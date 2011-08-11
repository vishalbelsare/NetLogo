package org.nlogo.prim.threed

import org.nlogo.agent.Turtle3D
import org.nlogo.api.LogoException
import org.nlogo.nvm.{ Command, Context, Syntax }

class _rollright extends Command {
  override def syntax =
    Syntax.commandSyntax(Array(Syntax.TYPE_NUMBER), "-T--", true)
  override def perform(context: Context) {
    val delta = argEvalDoubleValue(context, 0)
    val t = context.agent.asInstanceOf[Turtle3D]
    t.roll(t.roll + delta)
    context.ip = next
  }
}
