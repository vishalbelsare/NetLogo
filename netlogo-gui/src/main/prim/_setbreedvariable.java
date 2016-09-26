// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim;

import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.nvm.Command;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.RuntimePrimitiveException;

public final strictfp class _setbreedvariable
    extends Command {
  private final String name;

  public _setbreedvariable(_breedvariable original) {
    name = original.name();
    this.switches = true;
  }



  @Override
  public String toString() {
    return super.toString() + ":" + name;
  }

  @Override
  public void perform(final Context context)
      throws LogoException {
    Object value = args[0].report(context);
    try {
      context.agent.setBreedVariable(name, value);
    } catch (org.nlogo.api.AgentException ex) {
      throw new RuntimePrimitiveException
          (context, this, ex.getMessage());
    }
    context.ip = next;
  }
}
