"""
IPS SDK - 文件输入测试
"""

import io
import tempfile
from pathlib import Path

import pytest

from ips_sdk.http import FileInput, MIME_TYPE_MAP


class TestFileInput:
    """文件输入测试"""
    
    def test_file_path_string(self):
        """测试字符串路径"""
        fi = FileInput(file="test.jpg")
        assert fi.filename == "test.jpg"
        assert fi.content_type == "image/jpeg"
    
    def test_file_path_pathlib(self):
        """测试 Path 对象"""
        fi = FileInput(file=Path("test.png"))
        assert fi.filename == "test.png"
        assert fi.content_type == "image/png"
    
    def test_file_bytes(self):
        """测试 bytes 输入"""
        data = b"fake image data"
        fi = FileInput(file=data, filename="test.jpg")
        assert fi.filename == "test.jpg"
        assert fi.read() == data
    
    def test_file_io_object(self):
        """测试 IO 对象"""
        data = b"io test data"
        bio = io.BytesIO(data)
        fi = FileInput(file=bio, filename="test.bin")
        assert fi.filename == "test.bin"
        assert fi.read() == data
    
    def test_file_with_open_binary(self):
        """测试 open() 返回的文件对象"""
        data = b"open file test"
        with tempfile.NamedTemporaryFile(delete=False) as f:
            f.write(data)
            temp_path = f.name
        
        try:
            with open(temp_path, "rb") as f:
                fi = FileInput(file=f, filename="temp.jpg")
                assert fi.filename == "temp.jpg"
                assert fi.read() == data
        finally:
            Path(temp_path).unlink(missing_ok=True)
    
    def test_content_type_detection_jpg(self):
        """测试 MIME 类型 - JPEG"""
        assert FileInput._guess_content_type("test.jpg") == "image/jpeg"
        assert FileInput._guess_content_type("test.jpeg") == "image/jpeg"
    
    def test_content_type_detection_png(self):
        """测试 MIME 类型 - PNG"""
        assert FileInput._guess_content_type("test.png") == "image/png"
    
    def test_content_type_detection_pdf(self):
        """测试 MIME 类型 - PDF"""
        assert FileInput._guess_content_type("doc.pdf") == "application/pdf"
    
    def test_content_type_detection_gif(self):
        """测试 MIME 类型 - GIF"""
        assert FileInput._guess_content_type("anim.gif") == "image/gif"
    
    def test_content_type_detection_bmp(self):
        """测试 MIME 类型 - BMP"""
        assert FileInput._guess_content_type("image.bmp") == "image/bmp"
    
    def test_content_type_detection_webp(self):
        """测试 MIME 类型 - WebP"""
        assert FileInput._guess_content_type("photo.webp") == "image/webp"
    
    def test_content_type_detection_unknown(self):
        """测试 MIME 类型 - 未知类型"""
        assert FileInput._guess_content_type("file.abc") == "application/octet-stream"
        assert FileInput._guess_content_type("file.xyz") == "application/octet-stream"
    
    def test_content_type_case_insensitive(self):
        """测试 MIME 类型 - 大小写不敏感"""
        assert FileInput._guess_content_type("test.JPG") == "image/jpeg"
        assert FileInput._guess_content_type("test.PDF") == "application/pdf"
    
    def test_custom_content_type(self):
        """测试自定义 content_type"""
        fi = FileInput(file="test.jpg", content_type="image/png")
        assert fi.content_type == "image/png"
    
    def test_file_not_found(self):
        """测试文件不存在"""
        fi = FileInput(file="nonexistent_file.jpg")
        with pytest.raises(FileNotFoundError):
            fi.read()
    
    def test_file_repr(self):
        """测试 __repr__"""
        fi = FileInput(file="test.jpg")
        repr_str = repr(fi)
        assert "test.jpg" in repr_str
        assert "image/jpeg" in repr_str
    
    def test_bytes_input_no_filename(self):
        """测试 bytes 输入无 filename"""
        fi = FileInput(file=b"data")
        assert fi.filename == "file"  # 默认值
