package com.casesoft.dmc.controller.syn.third.basic;

import com.casesoft.dmc.core.vo.MessageBox;

/**
 * Created by pc on 2016-12-18.
 */
public interface ISynProductController {

    public MessageBox synchronizeStyle();

    public MessageBox synchronizeColor();

    public MessageBox synchronizeSize();

    public MessageBox synchronizeStyleSort();

    public MessageBox synchronizeSizeSort();

    public MessageBox synchronizeColorGroup();

    public MessageBox synchronizeProduct();

    public MessageBox synchronizePropertyKey();

    public MessageBox synchronizeFitting();

    public MessageBox synchronize();
}
