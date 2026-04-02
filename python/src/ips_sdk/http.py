"""
IPS SDK Python - HTTP 客户端封装
"""

import io
from pathlib import Path
from typing import Any, Dict, Optional, Union

import httpx

from .exceptions import IpsApiException, IpsNetworkException


# MIME 类型映射
MIME_TYPE_MAP: Dict[str, str] = {
    ".jpg": "image/jpeg",
    ".jpeg": "image/jpeg",
    ".png": "image/png",
    ".gif": "image/gif",
    ".pdf": "application/pdf",
    ".bmp": "image/bmp",
    ".webp": "image/webp",
}


class FileInput:
    """
    统一文件输入封装，支持多种输入方式：
    - str/Path: 文件路径
    - bytes: 原始字节数据
    - IO[bytes]: 文件对象
    """
    
    def __init__(
        self,
        file: Union[str, Path, bytes, io.IOBase],
        filename: Optional[str] = None,
        content_type: Optional[str] = None
    ):
        self._original = file
        self._content: Optional[bytes] = None
        
        # 自动推断 filename
        if filename is None:
            if isinstance(file, (str, Path)):
                filename = str(file)
            else:
                filename = "file"
        
        self.filename = filename
        
        # 自动推断 content_type
        if content_type is None:
            content_type = self._guess_content_type(self.filename)
        self.content_type = content_type
    
    @staticmethod
    def _guess_content_type(filename: str) -> str:
        """根据文件扩展名推断 MIME 类型"""
        ext = Path(filename).suffix.lower()
        return MIME_TYPE_MAP.get(ext, "application/octet-stream")
    
    def read(self) -> bytes:
        """读取文件内容"""
        if self._content is not None:
            return self._content
        
        if isinstance(self._original, bytes):
            self._content = self._original
        elif isinstance(self._original, (str, Path)):
            path = Path(self._original)
            if not path.exists():
                raise FileNotFoundError(f"文件不存在: {path}")
            self._content = path.read_bytes()
        elif hasattr(self._original, "read"):
            # IO 对象
            if hasattr(self._original, "seek") and hasattr(self._original, "tell"):
                self._original.seek(0)
            self._content = self._original.read()
        else:
            raise ValueError(f"不支持的文件类型: {type(self._original)}")
        
        return self._content
    
    def __repr__(self) -> str:
        return f"FileInput(filename={self.filename!r}, content_type={self.content_type!r})"


class HttpClient:
    """HTTP 请求客户端"""
    
    def __init__(
        self,
        base_url: str = "http://localhost:8000",
        timeout: float = 30.0
    ):
        self.base_url = base_url.rstrip("/")
        self.timeout = timeout
        self._client: Optional[httpx.Client] = None
    
    def _get_client(self) -> httpx.Client:
        """获取或创建 HTTP 客户端"""
        if self._client is None:
            self._client = httpx.Client(
                base_url=self.base_url,
                timeout=self.timeout
            )
        return self._client
    
    def close(self) -> None:
        """关闭 HTTP 客户端"""
        if self._client is not None:
            self._client.close()
            self._client = None
    
    def __enter__(self) -> "HttpClient":
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb) -> None:
        self.close()
    
    def _handle_response(self, response: httpx.Response) -> Dict[str, Any]:
        """处理 HTTP 响应"""
        if response.status_code >= 400:
            raise IpsApiException(
                status_code=response.status_code,
                message=f"API 错误: {response.status_code}",
                response_body=response.text
            )
        
        try:
            return response.json()
        except Exception:
            return {"data": response.text}
    
    def post_json(
        self,
        path: str,
        data: Dict[str, Any]
    ) -> Dict[str, Any]:
        """POST JSON 请求"""
        try:
            client = self._get_client()
            response = client.post(path, json=data)
            return self._handle_response(response)
        except httpx.ConnectError as e:
            raise IpsNetworkException(f"连接失败: {e}")
        except httpx.TimeoutException as e:
            raise IpsNetworkException(f"请求超时: {e}")
        except IpsApiException:
            raise
        except Exception as e:
            raise IpsNetworkException(str(e))
    
    def upload_file(
        self,
        path: str,
        file_input: FileInput,
        params: Optional[Dict[str, Any]] = None
    ) -> Dict[str, Any]:
        """文件上传请求 (multipart/form-data)"""
        try:
            client = self._get_client()
            
            files = {
                "file": (
                    file_input.filename,
                    file_input.read(),
                    file_input.content_type
                )
            }
            
            data = {}
            if params:
                # 过滤掉 None 值
                data = {k: str(v) for k, v in params.items() if v is not None}
            
            response = client.post(path, files=files, data=data)
            return self._handle_response(response)
        except httpx.ConnectError as e:
            raise IpsNetworkException(f"连接失败: {e}")
        except httpx.TimeoutException as e:
            raise IpsNetworkException(f"请求超时: {e}")
        except IpsApiException:
            raise
        except FileNotFoundError:
            raise
        except Exception as e:
            raise IpsNetworkException(str(e))
    
    def get(self, path: str) -> Dict[str, Any]:
        """GET 请求"""
        try:
            client = self._get_client()
            response = client.get(path)
            return self._handle_response(response)
        except httpx.ConnectError as e:
            raise IpsNetworkException(f"连接失败: {e}")
        except httpx.TimeoutException as e:
            raise IpsNetworkException(f"请求超时: {e}")
        except IpsApiException:
            raise
        except Exception as e:
            raise IpsNetworkException(str(e))
