package com.weqia.utils.dialog.quickaction;

import com.weqia.data.UtilData;

/**
 * Listener for item click
 */
public abstract class OnActionItemClickListener {
    public UtilData utilData;

    public OnActionItemClickListener(UtilData utilData) {
        this.utilData = utilData;
    }

    public abstract void onItemClick(QuickAction source, int pos, int actionId);
}
