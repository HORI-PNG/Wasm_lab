/*
 * Created on 2007/12/13
 * Copyright (C) 2007 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matx;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;


/**
 * MaTXの型を表す抽象クラスです。
 * 
 * @author koga
 * @version $Revision: 1.2 $, 2007/12/13
 */
public abstract class MatxAbstractObject implements MatxObject {

  /**
   * {@inheritDoc}
   */
  @Override
  public final void writeMmFormat(final File file, final String name) throws IOException {
    try (final Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")))) { //$NON-NLS-1$
      writeMmFormat(output, name, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void writeMxFormat(final File file, final String dataName) throws IOException {
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
      writeMxFormat(output, dataName);
    }
  }
}
