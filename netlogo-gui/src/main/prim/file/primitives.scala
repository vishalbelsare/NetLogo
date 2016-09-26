// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.file

import org.nlogo.core.{ CompilerException, FileMode }
import org.nlogo.api.{ OutputDestination }
import org.nlogo.core.Syntax
import org.nlogo.nvm.{ Command, Context, Reporter, Workspace }
import org.nlogo.nvm.RuntimePrimitiveException
import java.io.IOException

class _fileatend extends Reporter {

  override def report(context: Context) =
    try Boolean.box(workspace.fileManager.eof)
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
}

class _fileclose extends Command {

  override def perform(context: Context) {
    try
      if (workspace.fileManager.hasCurrentFile)
        workspace.fileManager.closeCurrentFile()
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    context.ip = next
  }
}

class _filecloseall extends Command {

  override def perform(context: Context) {
    try workspace.fileManager.closeAllFiles()
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    context.ip = next
  }
}

class _filedelete extends Command {

  override def perform(context: Context) {
    val argString = argEvalString(context, 0)
    try
      workspace.fileManager.deleteFile(
        workspace.fileManager.attachPrefix(argString))
    catch {
      case ex: java.net.MalformedURLException =>
        throw new RuntimePrimitiveException(
          context, this, argString +
          " is not a valid path name: " + ex.getMessage)
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    context.ip = next
  }
}

class _fileexists extends Reporter {

  override def report(context: Context) = {
    val argString = argEvalString(context, 0)
    try
      Boolean.box(
        workspace.fileManager.fileExists(
          workspace.fileManager.attachPrefix(argString)))
    catch {
      case ex: java.net.MalformedURLException =>
        throw new RuntimePrimitiveException(
          context, this, argString +
            " is not a valid path name: " + ex.getMessage)
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
  }
}

class _fileflush extends Command {

  override def perform(context: Context) {
    try
      if (workspace.fileManager.hasCurrentFile)
        workspace.fileManager.flushCurrentFile()
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    context.ip = next
  }
}

class _fileopen extends Command {

  override def perform(context: Context) {
    try
      // DefaultFileManager.openFile attaches the prefix for us, so we need not normalize our path
      // before calling that method - CLB 05/17/05
      workspace.fileManager.openFile(
        argEvalString(context, 0))
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    context.ip = next
  }
}

class _fileprint extends Command {

  override def perform(context: Context) {
    try
      workspace.fileManager.ensureMode(
        org.nlogo.core.FileMode.Append)
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    workspace.outputObject(
      args(0).report(context), null, true, false,
      OutputDestination.File)
    context.ip = next
  }
}

class _fileread extends Reporter {

  override def report(context: Context) =
    try workspace.fileManager.read(world)
    catch {
      case ex: CompilerException =>
        throw new RuntimePrimitiveException(
          context, this,
          ex.getMessage + workspace.fileManager.getErrorInfo)
      case ex: java.io.EOFException =>
        throw new RuntimePrimitiveException(
          context, this, "The end of file has been reached")
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
}

class _filereadchars extends Reporter {

  override def report(context: Context) =
    try workspace.fileManager.readChars(argEvalIntValue(context, 0))
    catch {
      case _: java.io.EOFException =>
        throw new RuntimePrimitiveException(
          context, this, "The end of file has been reached")
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
}

class _filereadline extends Reporter {

  override def report(context: Context) =
    try workspace.fileManager.readLine()
    catch {
      case _: java.io.EOFException =>
        throw new RuntimePrimitiveException(
          context, this, "The end of file has been reached")
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
}

class _fileshow extends Command {

  override def perform(context: Context) {
    val s = args(0).report(context)
    try
      workspace.fileManager.ensureMode(FileMode.Append)
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    workspace.outputObject(s, context.agent, true, true,
                           OutputDestination.File)
    context.ip = next
  }
}

class _filetype extends Command {

  override def perform(context: Context) {
    val s = args(0).report(context)
    try workspace.fileManager.ensureMode(FileMode.Append)
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    workspace.outputObject(s, null, false, false,
                           OutputDestination.File)
    context.ip = next
  }
}

class _filewrite extends Command {

  override def perform(context: Context) {
    val s = args(0).report(context)
    try
      workspace.fileManager.ensureMode(FileMode.Append)
    catch {
      case ex: IOException =>
        throw new RuntimePrimitiveException(context, this, ex.getMessage)
    }
    workspace.outputObject(s, null, false, true,
                           OutputDestination.File)
    context.ip = next
  }
}
