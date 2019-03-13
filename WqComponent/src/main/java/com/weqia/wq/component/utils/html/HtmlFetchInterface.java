package com.weqia.wq.component.utils.html;

import com.weqia.wq.data.html.LinksData;

public interface HtmlFetchInterface {
    public void fetchComplete(LinksData LinkData);
    
    public void fetchError();
    
    public void fetchCancel();
}
