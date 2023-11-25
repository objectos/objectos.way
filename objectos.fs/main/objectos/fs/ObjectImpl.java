package objectos.fs;

class ObjectImpl extends ObjectJava7 {

  ObjectImpl(java.io.File file) {
    super(file);
  }

  ObjectImpl(java.nio.file.Path delegate) {
    super(delegate);
  }

}