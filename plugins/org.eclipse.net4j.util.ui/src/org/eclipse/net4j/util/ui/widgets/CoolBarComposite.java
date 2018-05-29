/*
 * Copyright (c) 2008, 2011, 2012, 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.ui.widgets;

import org.eclipse.net4j.util.ui.UIUtil;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

/**
 * @author Eike Stepper
 * @since 2.0
 */
public abstract class CoolBarComposite extends Composite
{
  private CoolBarManager coolBarManager;

  private Control clientControl;

  private Label separator;

  private ToolBarManager toolBarManager;

  public CoolBarComposite(Composite parent, int style)
  {
    super(parent, style);
    setLayout(new CoolBarLayout());

    toolBarManager = new ToolBarManager(SWT.FLAT | SWT.RIGHT | SWT.WRAP | SWT.HORIZONTAL);
    fillCoolBar(toolBarManager);

    coolBarManager = new CoolBarManager(SWT.FLAT | SWT.HORIZONTAL);
    coolBarManager.add(toolBarManager);
    coolBarManager.setLockLayout(true);
    coolBarManager.createControl(this);
    coolBarManager.update(true);

    separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
    separator.setLayoutData(UIUtil.createGridData(true, false));

    Composite composite = new Composite(this, SWT.NONE);
    composite.setLayout(UIUtil.createGridLayout(1));

    clientControl = createUI(composite);
    clientControl.setLayoutData(UIUtil.createGridData());
  }

  @Override
  public void dispose()
  {
    coolBarManager.dispose();
    super.dispose();
  }

  public Control getClientControl()
  {
    return clientControl;
  }

  public void updateCoolBar()
  {
    toolBarManager.removeAll();
    fillCoolBar(toolBarManager);
    coolBarManager.update(true);
  }

  protected abstract Control createUI(Composite parent);

  protected CoolBar getCoolBarControl()
  {
    if (coolBarManager != null)
    {
      return coolBarManager.getControl();
    }

    return null;
  }

  protected void fillCoolBar(IContributionManager manager)
  {
  }

  /**
   * @author Eike Stepper
   */
  public class CoolBarLayout extends Layout
  {
    static final int BAR_SIZE = 23;

    private static final int SEPARATOR_SIZE = 1;

    @Override
    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache)
    {
      if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT)
      {
        return new Point(wHint, hHint);
      }

      boolean coolBarChildrenExist = coolBarChildrenExist();
      Point result = new Point(0, 0);
      Control[] ws = composite.getChildren();
      for (int i = 0; i < ws.length; i++)
      {
        Control w = ws[i];
        boolean hide = false;
        if (w == getCoolBarControl())
        {
          if (!coolBarChildrenExist)
          {
            hide = true;
            result.y += BAR_SIZE;
          }
        }
        else if (w == separator)
        {
          if (!coolBarChildrenExist)
          {
            hide = true;
            result.y += SEPARATOR_SIZE;
          }
        }
        else if (i > 0)
        {
          hide = false;
        }

        if (!hide)
        {
          Point e = w.computeSize(wHint, hHint, flushCache);
          result.x = Math.max(result.x, e.x);
          result.y += e.y;
        }
      }

      if (wHint != SWT.DEFAULT)
      {
        result.x = wHint;
      }

      if (hHint != SWT.DEFAULT)
      {
        result.y = hHint;
      }

      return result;
    }

    @Override
    protected void layout(Composite composite, boolean flushCache)
    {
      boolean coolBarChildrenExist = coolBarChildrenExist();
      Rectangle clientArea = composite.getClientArea();
      Control[] ws = composite.getChildren();
      for (int i = 0; i < ws.length; i++)
      {
        Control w = ws[i];
        if (w == getCoolBarControl() || w == separator)
        {
          if (coolBarChildrenExist)
          {
            Point e = w.computeSize(clientArea.width, SWT.DEFAULT, flushCache);
            w.setBounds(clientArea.x, clientArea.y, clientArea.width, e.y);
            clientArea.y += e.y;
            clientArea.height -= e.y;
          }
        }
        else
        {
          w.setBounds(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
        }
      }
    }

    protected boolean coolBarChildrenExist()
    {
      CoolBar coolBarControl = getCoolBarControl();
      if (coolBarControl != null)
      {
        return coolBarControl.getItemCount() > 0;
      }

      return false;
    }
  }
}
