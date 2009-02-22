/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

Ext.ux.WindowHtmlEditor = Ext.extend(Ext.form.Field, {

    defaultAutoCreate : { tag: "input", type:'hidden', autocomplete: "off"},
    saveActionText: 'Save',
    cancelActionText: 'Cancel',
    emptyMessage: 'Doble click here to edit.',

    onRender : function(ct, position){
        Ext.ux.WindowHtmlEditor.superclass.onRender.call(this, ct, position);

        this.wrap = this.el.insertSibling({tag:'div'});

        if(this.inputValue !== undefined && this.inputValue != ''){
            this.el.dom.value = this.inputValue;
            this.wrap.update(this.inputValue);
        }else{
            this.wrap.update(this.emptyMessage);
        }

        var htmlEditor = new Ext.form.HtmlEditor({
                id: this.id + '-editor',
                anchor: '100% 100%',
                value: this.inputValue,
                defaultFont:'arial'
            });

        var wrap = this.wrap;
        var windowEditor = this.window;
        var editor = this;
        this.window = new Ext.Window({
            id: this.id + '-window',
            width: 500,
            height: 300,
            title: this.fieldLabel,
            layout:'anchor',
            items: htmlEditor,
            buttons:[{
                text: this.saveActionText,
                handler: function(){
                    wrap.update(htmlEditor.getValue());
                    editor.setValue(htmlEditor.getValue());
                    windowEditor.hide();
                }
            },
                    {
                text: this.cancelActionText,
                handler:function(){
                    windowEditor.hide();
                }
            }]
        });
        windowEditor = this.window;
    },

    initEvents : function(){
        Ext.ux.WindowHtmlEditor.superclass.initEvents.call(this);
        this.wrap.on("dblclick", this.onDblClick,  this);
    },

    onDblClick : function(){
        this.window.show();
    }
});

Ext.reg('windowhtmleditor', Ext.ux.WindowHtmlEditor);

Ext.ux.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.ux.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:180,
    hasSearch : false,
    paramName : 'query',
    pageSize : 10,

    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            var o = {start: 0, limit: this.pageSize};
            this.store.baseParams = this.store.baseParams || {};
            this.store.baseParams[this.paramName] = '';
            this.store.reload({params:o});
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        var o = {start: 0, limit:this.pageSize};
        this.store.baseParams = this.store.baseParams || {};
        this.store.baseParams[this.paramName] = v;
        this.store.reload({params:o});
        this.hasSearch = true;
        this.triggers[0].show();
    }
});

Ext.reg('searchfield', Ext.ux.SearchField);

Ext.ux.FileField = Ext.extend(Ext.form.TextField, {
    defaultAutoCreate : { tag: "input", type:'file', autocomplete: "off"}
});

Ext.reg('filefield', Ext.ux.FileField);

Ext.form.ComboBox.prototype.onRender = function(ct, position){
    Ext.form.ComboBox.superclass.onRender.call(this, ct, position);
    if(this.hiddenName){
        this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName, id: (this.hiddenId||this.hiddenName)},
                'before', true);
        this.hiddenField.value =
            this.hiddenValue !== undefined ? this.hiddenValue :
            this.value !== undefined ? this.value : '';

                    this.el.dom.removeAttribute('name');
    }
    if(Ext.isGecko){
        this.el.dom.setAttribute('autocomplete', 'off');
    }

    if(!this.lazyInit){
        this.initList();
        this.initValue();
    }else{
        this.on('focus', this.initList, this, {single: true});
    }

    if(!this.editable){
        this.editable = true;
        this.setEditable(false);
    }
}

Ext.form.ComboBox.prototype.setValue = function(v){
    var text = v;
    if(this.valueField){
        var r = this.findRecord(this.valueField, v);
        if(r){
            text = r.data[this.displayField];
        }else if(this.text !== undefined){
            text = this.text;
        }else if(this.valueNotFoundText !== undefined){
            text = this.valueNotFoundText;
        }
    }
    this.lastSelectionText = text;
    if(this.hiddenField){
        this.hiddenField.value = v;
    }
    Ext.form.ComboBox.superclass.setValue.call(this, text);
    this.value = v;
}

Ext.form.Action.Submit.prototype.getEntityId = function(){
    var root = this.response.responseXML.documentElement || this.response.responseXML;
    return Ext.DomQuery.selectValue('@entityId', root, true);
}

Ext.namespace('Ext.ux.layout');

/**
 */

Ext.ux.layout.RowFitLayout = Ext.extend(Ext.layout.ContainerLayout, {
  // private
  monitorResize: true,

  // private
  trackChildEvents: ['collapse', 'expand', 'hide', 'show'],

  // private
  renderAll: function(ct, target) {
    Ext.ux.layout.RowFitLayout.superclass.renderAll.apply(this, arguments);
    // add event listeners on addition/removal of children
    ct.on('add', this.containerListener);
    ct.on('remove', this.containerListener);
  },

  // private
  renderItem: function(c, position, target) {
    Ext.ux.layout.RowFitLayout.superclass.renderItem.apply(this, arguments);

    // add event listeners
    for (var i=0, n = this.trackChildEvents.length; i < n; i++) {
      c.on(this.trackChildEvents[i], this.itemListener);
    }
    c.animCollapse = false; // looks ugly together with row-fit layout

    // store some layout-specific calculations
    c.rowFit = {
      hasAbsHeight: false, // whether the component has absolute height (in pixels)
      relHeight: 0, // relative height, in pixels (if applicable)
      calcRelHeight: 0, // calculated relative height (used when element is resized)
      calcAbsHeight: 0 // calculated absolute height
    };

    // process height config option
    if (c.height) {
      // store relative (given in percent) height
      if (typeof c.height == "string" && c.height.indexOf("%")) {
        c.rowFit.relHeight = parseInt(c.height);
      }
      else { // set absolute height
        c.setHeight(c.height);
        c.rowFit.hasAbsHeight = true;
      }
    }
  },

  // private
  onLayout: function(ct, target) {
    Ext.ux.layout.RowFitLayout.superclass.onLayout.call(this, ct, target);

    if (this.container.collapsed || !ct.items || !ct.items.length) { return; }

    // first loop: determine how many elements with relative height are there,
    // sums of absolute and relative heights etc.
    var absHeightSum = 0, // sum of elements' absolute heights
        relHeightSum = 0, // sum of all percent heights given in children configs
        relHeightRatio = 1, // "scale" ratio used in case sum <> 100%
        relHeightElements = [], // array of elements with 'relative' height for the second loop
        noHeightCount = 0; // number of elements with no height given

    for (var i=0, n = ct.items.length; i < n; i++) {
      var c = ct.items.itemAt(i);

      if (!c.isVisible()) { continue; }

      // collapsed panel is treated as an element with absolute height
      if (c.collapsed) { absHeightSum += c.getFrameHeight(); }
      // element that has an absolute height
      else if (c.rowFit.hasAbsHeight) {
        absHeightSum += c.height;
      }
      // 'relative-heighted'
      else {
        if (!c.rowFit.relHeight) { noHeightCount++; } // element with no height given
        else { relHeightSum += c.rowFit.relHeight; }
        relHeightElements.push(c);
      }
    }

    // if sum of relative heights <> 100% (e.g. error in config or consequence
    // of collapsing/removing panels), scale 'em so it becomes 100%
    if (noHeightCount == 0 && relHeightSum != 100) {
      relHeightRatio = 100 / relHeightSum;
    }

    var freeHeight = target.getStyleSize().height - absHeightSum, // "unallocated" height we have
        absHeightLeft = freeHeight; // track how much free space we have

    while (relHeightElements.length) {
      var c = relHeightElements.shift(), // element we're working with
          relH = c.rowFit.relHeight * relHeightRatio, // height of this element in percent
          absH = 0; // height in pixels

      // no height in config
      if (!relH) {
        relH = (100 - relHeightSum) / noHeightCount;
      }

      // last element takes all remaining space
      if (!relHeightElements.length) { absH = absHeightLeft; }
      else { absH = Math.round(freeHeight * relH / 100); }

      // anyway, height can't be negative
      if (absH < 0) { absH = 0; }

      c.rowFit.calcAbsHeight = absH;
      c.rowFit.calcRelHeight = relH;

      c.setHeight(absH);
      absHeightLeft -= absH;
    }

  },


  /**
   */
  itemListener: function(item) {
    item.ownerCt.doLayout();
  },


  /**
   */
  containerListener: function(ct) {
    ct.doLayout();
  }

});

// Split adapter
if (Ext.SplitBar.BasicLayoutAdapter) {

  /**
   */
  Ext.ux.layout.RowFitLayout.SplitAdapter = function(splitbar) {
    if (splitbar && splitbar.el.dom.nextSibling) {
      var next = Ext.getCmp( splitbar.el.dom.nextSibling.id ),
          resized = Ext.getCmp(splitbar.resizingEl.id);

      if (next) {
        splitbar.maxSize = (resized.height || resized.rowFit.calcAbsHeight) +
                           next.getInnerHeight() - 1; // seems can't set height=0 in IE, "1" works fine
      }
      splitbar.minSize = resized.getFrameHeight() + 1;
    }
  }

  Ext.extend(Ext.ux.layout.RowFitLayout.SplitAdapter, Ext.SplitBar.BasicLayoutAdapter, {

    setElementSize: function(splitbar, newSize, onComplete) {
      var resized = Ext.getCmp(splitbar.resizingEl.id);

      // can't resize absent, collapsed or hidden panel
      if (!resized || resized.collapsed || !resized.isVisible()) return;

      // resizingEl has absolute height: just change it
      if (resized.rowFit.hasAbsHeight) {
        resized.setHeight(newSize);
      }
      // resizingEl has relative height: affects next sibling
      else {
        if (splitbar.el.dom.nextSibling) {
          var nextSibling = Ext.getCmp( splitbar.el.dom.nextSibling.id ),
              deltaAbsHeight = newSize - resized.rowFit.calcAbsHeight, // pixels
              nsRf = nextSibling.rowFit, // shortcut
              rzRf = resized.rowFit,
              // pixels in a percent
              pctPxRatio = rzRf.calcRelHeight / rzRf.calcAbsHeight,
              deltaRelHeight = pctPxRatio * deltaAbsHeight; // change in height in percent

          rzRf.relHeight = rzRf.calcRelHeight + deltaRelHeight;

          if (nsRf.hasAbsHeight) {
            var newHeight = nextSibling.height - deltaAbsHeight;
            nextSibling.height = newHeight;
            nextSibling.setHeight(newHeight);
          }
          else {
            nsRf.relHeight = nsRf.calcRelHeight - deltaRelHeight;
          }
        }
      }
      // recalculate heights
      resized.ownerCt.doLayout();
    } // of setElementSize

  }); // of SplitAdapter
}

Ext.Container.LAYOUTS['row-fit'] = Ext.ux.layout.RowFitLayout;


Ext.tree.ColumnTree = Ext.extend(Ext.tree.TreePanel, {
    lines:false,
    borderWidth: Ext.isBorderBox ? 0 : 2, // the combined left/right border for each cell
    cls:'x-column-tree',

    onRender : function(){
        Ext.tree.ColumnTree.superclass.onRender.apply(this, arguments);
        this.headers = this.body.createChild(
            {cls:'x-tree-headers'},this.innerCt.dom);

        var cols = this.columns, c;
        var totalWidth = 0;

        for(var i = 0, len = cols.length; i < len; i++){
             c = cols[i];
             totalWidth += c.width;
             this.headers.createChild({
                 cls:'x-tree-hd ' + (c.cls?c.cls+'-hd':''),
                 cn: {
                     cls:'x-tree-hd-text',
                     html: c.header
                 },
                 style:'width:'+(c.width-this.borderWidth)+'px;'
             });
        }
        this.headers.createChild({cls:'x-clear'});
        // prevent floats from wrapping when clipped
        this.headers.setWidth(totalWidth);
        this.innerCt.setWidth(totalWidth);
    }
});

Ext.tree.ColumnNodeUI = Ext.extend(Ext.tree.TreeNodeUI, {
    focus: Ext.emptyFn, // prevent odd scrolling behavior

    renderElements : function(n, a, targetNode, bulkRender){
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';

        var t = n.getOwnerTree();
        var cols = t.columns;
        var bw = t.borderWidth;
        var c = cols[0];

        var buf = [
             '<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf ', a.cls,'">',
                '<div class="x-tree-col" style="width:',c.width-bw,'px;">',
                    '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
                    '<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow">',
                    '<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on">',
                    '<a hidefocus="on" class="x-tree-node-anchor" href="',a.href ? a.href : "#",'" tabIndex="1" ',
                    a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '>',
                    '<span unselectable="on">', n.text || (c.renderer ? c.renderer(a[c.dataIndex], n, a) : a[c.dataIndex]),"</span></a>",
                "</div>"];
         for(var i = 1, len = cols.length; i < len; i++){
             c = cols[i];

             buf.push('<div class="x-tree-col ',(c.cls?c.cls:''),'" style="width:',c.width-bw,'px;">',
                        '<div class="x-tree-col-text">',(c.renderer ? c.renderer(a[c.dataIndex], n, a) : a[c.dataIndex]),"</div>",
                      "</div>");
         }
         buf.push(
            '<div class="x-clear"></div></div>',
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
            "</li>");

        if(bulkRender !== true && n.nextSibling && n.nextSibling.ui.getEl()){
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin",
                                n.nextSibling.ui.getEl(), buf.join(""));
        }else{
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf.join(""));
        }

        this.elNode = this.wrap.childNodes[0];
        this.ctNode = this.wrap.childNodes[1];
        var cs = this.elNode.firstChild.childNodes;
        this.indentNode = cs[0];
        this.ecNode = cs[1];
        this.iconNode = cs[2];
        this.anchor = cs[3];
        this.textNode = cs[3].firstChild;
    }
});

/*
 */

Ext.grid.RowExpander = function(config){
    Ext.apply(this, config);

    this.addEvents({
        beforeexpand : true,
        expand: true,
        beforecollapse: true,
        collapse: true
    });

    Ext.grid.RowExpander.superclass.constructor.call(this);

    if(this.tpl){
        if(typeof this.tpl == 'string'){
            this.tpl = new Ext.Template(this.tpl);
        }
        this.tpl.compile();
    }

    this.state = {};
    this.bodyContent = {};
};

Ext.extend(Ext.grid.RowExpander, Ext.util.Observable, {
    header: "",
    width: 20,
    sortable: false,
    fixed:true,
    dataIndex: '',
    id: 'expander',
    lazyRender : true,
    enableCaching: true,

    getRowClass : function(record, rowIndex, p, ds){
        p.cols = p.cols-1;
        var content = this.bodyContent[record.id];
        if(!content && !this.lazyRender){
            content = this.getBodyContent(record, rowIndex);
        }
        if(content){
            p.body = content;
        }
        return this.state[record.id] ? 'x-grid3-row-expanded' : 'x-grid3-row-collapsed';
    },

    init : function(grid){
        this.grid = grid;

        var view = grid.getView();
        view.getRowClass = this.getRowClass.createDelegate(this);

        view.enableRowBody = true;

        grid.on('render', function(){
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    getBodyContent : function(record, index){
        if(!this.enableCaching){
            return this.tpl.apply(record.data);
        }
        var content = this.bodyContent[record.id];
        if(!content){
            content = this.tpl.apply(record.data);
            this.bodyContent[record.id] = content;
        }
        return content;
    },

    onMouseDown : function(e, t){
        if(t.className == 'x-grid3-row-expander'){
            e.stopEvent();
            var row = e.getTarget('.x-grid3-row');
            this.toggleRow(row);
        }
    },

    renderer : function(v, p, record){
        p.cellAttr = 'rowspan="2"';
        return '<div class="x-grid3-row-expander">&#160;</div>';
    },

    beforeExpand : function(record, body, rowIndex){
        if(this.fireEvent('beforeexpand', this, record, body, rowIndex) !== false){
            if(this.tpl && this.lazyRender){
                body.innerHTML = this.getBodyContent(record, rowIndex);
            }
            return true;
        }else{
            return false;
        }
    },

    toggleRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        this[Ext.fly(row).hasClass('x-grid3-row-collapsed') ? 'expandRow' : 'collapseRow'](row);
    },

    expandRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
        var body = Ext.DomQuery.selectNode('tr:nth(2) div.x-grid3-row-body', row);
        if(this.beforeExpand(record, body, row.rowIndex)){
            this.state[record.id] = true;
            Ext.fly(row).replaceClass('x-grid3-row-collapsed', 'x-grid3-row-expanded');
            this.fireEvent('expand', this, record, body, row.rowIndex);
        }
    },

    collapseRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
        var body = Ext.fly(row).child('tr:nth(1) div.x-grid3-row-body', true);
        if(this.fireEvent('beforcollapse', this, record, body, row.rowIndex) !== false){
            this.state[record.id] = false;
            Ext.fly(row).replaceClass('x-grid3-row-expanded', 'x-grid3-row-collapsed');
            this.fireEvent('collapse', this, record, body, row.rowIndex);
        }
    }
});

/**
 */
Ext.namespace('Ext.ux');
Ext.ux.RadioGroup = Ext.extend(Ext.form.Field,  {
    /**
     */
    focusClass : undefined,
    /**
     */
    fieldClass: "x-form-field",
    /**
     */
    checked: false,
    /**
     */
    defaultAutoCreate : { tag: "input", type: 'radio', autocomplete: "off"},
    /**
     */

	getId:function(){
		//if multiple radios are defined use this information
		if(this.radios && this.radios instanceof Array){
			if(this.radios.length){
				var r=this.radios[0];
				this.value=r.value;
				this.boxLabel=r.boxLabel;
				this.checked=r.checked || false;
				this.readOnly=r.readOnly || false;
				this.disabled=r.disabled || false;
				this.tabIndex=r.tabIndex;
				this.cls=r.cls;
				this.listeners=r.listeners;
				this.style=r.style;
				this.bodyStyle=r.bodyStyle;
				this.hideParent=r.hideParent;
				this.hidden=r.hidden;
			}
		}
		Ext.ux.RadioGroup.superclass.getId.call(this);
	},

	// private
    initComponent : function(){
        Ext.ux.RadioGroup.superclass.initComponent.call(this);
        this.addEvents(
            /**
             */
            'check'
        );
    },

    // private
    onResize : function(){
        Ext.ux.RadioGroup.superclass.onResize.apply(this, arguments);
        if(!this.boxLabel){
            this.el.alignTo(this.wrap, 'c-c');
        }
    },

    // private
    initEvents : function(){
        Ext.ux.RadioGroup.superclass.initEvents.call(this);
        this.el.on("click", this.onClick,  this);
        this.el.on("change", this.onClick,  this);
    },

	// private
    getResizeEl : function(){
        return this.wrap;
    },

    // private
    getPositionEl : function(){
        return this.wrap;
    },

    /**
     */
    markInvalid : Ext.emptyFn,
    /**
     */
    clearInvalid : Ext.emptyFn,

    // private
    onRender : function(ct, position){
        Ext.ux.RadioGroup.superclass.onRender.call(this, ct, position);
        this.wrap = this.el.wrap({cls: "x-form-check-wrap"});
        if(this.boxLabel){
            this.wrap.createChild({tag: 'label', htmlFor: this.el.id, cls: 'x-form-cb-label', html: this.boxLabel});
        }
		if(!this.isInGroup){
			this.wrap.applyStyles({'padding-top':'2px'});
		}
        if(this.checked){
            this.setChecked(true);
        }else{
            this.checked = this.el.dom.checked;
        }
		if (this.radios && this.radios instanceof Array) {
			this.els=new Array();
			this.els[0]=this.el;
			for(var i=1;i<this.radios.length;i++){
				var r=this.radios[i];
				this.els[i]=new Ext.ux.RadioGroup({
					renderTo:this.wrap,
					hideLabel:true,
					boxLabel:r.boxLabel,
					checked:r.checked || false,
					value:r.value,
					name:this.name || this.id,
					readOnly:r.readOnly || false,
					disabled:r.disabled || false,
					tabIndex:r.tabIndex,
					cls:r.cls,
					listeners:r.listeners,
					style:r.style,
					bodyStyle:r.bodyStyle,
					hideParent:r.hideParent,
					hidden:r.hidden,
					isInGroup:true
				});
				if (this.horizontal) {
					this.els[i].el.up('div.x-form-check-wrap').applyStyles({
						'display': 'inline',
						'padding-left': '5px'
					});
				}
			}
			if(this.hidden)this.hide();
		}
    },

    initValue : function(){
        if(this.value !== undefined){
            this.el.dom.value=this.value;
        }else if(this.el.dom.value.length > 0){
            this.value=this.el.dom.value;
        }
    },

    // private
    onDestroy : function(){
		if (this.radios && this.radios instanceof Array) {
			var cnt = this.radios.length;
			for(var x=1;x<cnt;x++){
				this.els[x].destroy();
			}
		}
        if(this.wrap){
            this.wrap.remove();
        }
        Ext.ux.RadioGroup.superclass.onDestroy.call(this);
    },

	setChecked:function(v){
        if(this.el && this.el.dom){
			var fire = false;
			if(v != this.checked)fire=true;
			this.checked=v;
            this.el.dom.checked = this.checked;
            this.el.dom.defaultChecked = this.checked;
    	    if(fire)this.fireEvent("check", this, this.checked);
	    }
    },
    /**
     */
    getValue : function(){
        if(!this.rendered) {
            return this.value;
        }
        var p=this.el.up('form');//restrict to the form if it is in a form
		if(!p)p=Ext.getBody();
		var c=p.child('input[name='+this.el.dom.name+']:checked', true);
		return (c)?c.value:this.value;
    },

	// private
    onClick : function(){
        if(this.el.dom.checked != this.checked){
			var p = this.el.up('form');
			if (!p)
				p = Ext.getBody();
			var els = p.select('input[name=' + this.el.dom.name + ']');
			els.each(function(el){
				if (el.dom.id == this.id) {
					this.setChecked(true);
				}
				else {
					var e = Ext.getCmp(el.dom.id);
					e.setChecked.apply(e, [false]);
				}
			}, this);
        }
    },

    /**
     */

    setValue : function(v){
        if(!this.rendered) {
            this.value=v;
            return;
        }
        var p=this.el.up('form');//restrict to the form if it is in a form
        if(!p)p=Ext.getBody();
        var target = p.child('input[name=' + this.el.dom.name + '][value=' + v + ']', true);
        if (target) target.checked = true;
    }
});
Ext.reg('ux-radiogroup', Ext.ux.RadioGroup);

/**
 */
Ext.ux.CustomContentField = Ext.extend(Ext.form.Field, {

	// private
	defaultAutoCreate: {
		tag: 'div',
		cls: 'x-customcontent-field'
	},

	// private
	initComponent: function() {
		Ext.ux.CustomContentField.superclass.initComponent.call(this);
		this.addEvents(
			/**
			 */
			'load'
		);
	},

	// private
	initValue: Ext.emptyFn,

	// private
	onRender: function(ct, position){
        Ext.ux.CustomContentField.superclass.onRender.call(this, ct, position);

        this.contentCt = ct.select('div.x-customcontent-field').item(0);

        if (this.loadText)
        {
        	this.contentCt.update(this.loadText);
        }

		this.loadContent();
	},

	/**
	 */
	loadContent: function() {
		// clear out previous content
		this.contentCt.update('');

        if (this.directContent)
        {
        	this.contentCt.update(this.directContent, true);
        }
        else if (this.divContent)
        {
			el = Ext.get(this.divContent);
			if (el)
			{
				el.remove();
				this.contentCt.appendChild(el);
			}
        }
        else if (this.ajaxContent)
        {
			Ext.Ajax.request({
				url: this.ajaxContent,
				scope: this,
				success: function(response, request) {
					try
					{
						Ext.DomHelper.overwrite(this.contentCt, Ext.decode(response.responseText));
					}
					catch (e)
					{
						this.contentCt.update(response.responseText, true);
					}
				}
			});
        }

        if (this.autoShow)
        {
        	this.contentCt.first().show();
        }

        this.fireEvent('load', this);
	},

	/**
	 */
	reload: function() {
		this.loadContent();
	},

	/**
	 */
	getValue: function() {
		return this.contentCt;
	}
});
Ext.reg('ux-customcontent', Ext.ux.CustomContentField);


Ext.ux.Portal = Ext.extend(Ext.Panel, {
    layout: 'column',
    autoScroll:true,
    cls:'x-portal',
    defaultType: 'portalcolumn',

    initComponent : function(){
        Ext.ux.Portal.superclass.initComponent.call(this);
        this.addEvents({
            validatedrop:true,
            beforedragover:true,
            dragover:true,
            beforedrop:true,
            drop:true
        });
    },

    initEvents : function(){
        Ext.ux.Portal.superclass.initEvents.call(this);
        this.dd = new Ext.ux.Portal.DropZone(this, this.dropConfig);
    }
});
Ext.reg('portal', Ext.ux.Portal);


Ext.ux.Portal.DropZone = function(portal, cfg){
    this.portal = portal;
    Ext.dd.ScrollManager.register(portal.body);
    Ext.ux.Portal.DropZone.superclass.constructor.call(this, portal.bwrap.dom, cfg);
    portal.body.ddScrollConfig = this.ddScrollConfig;
};

Ext.extend(Ext.ux.Portal.DropZone, Ext.dd.DropTarget, {
    ddScrollConfig : {
        vthresh: 50,
        hthresh: -1,
        animate: true,
        increment: 200
    },

    createEvent : function(dd, e, data, col, c, pos){
        return {
            portal: this.portal,
            panel: data.panel,
            columnIndex: col,
            column: c,
            position: pos,
            data: data,
            source: dd,
            rawEvent: e,
            status: this.dropAllowed
        };
    },

    notifyOver : function(dd, e, data){
        var xy = e.getXY(), portal = this.portal, px = dd.proxy;

        // case column widths
        if(!this.grid){
            this.grid = this.getGrid();
        }

        // handle case scroll where scrollbars appear during drag
        var cw = portal.body.dom.clientWidth;
        if(!this.lastCW){
            this.lastCW = cw;
        }else if(this.lastCW != cw){
            this.lastCW = cw;
            portal.doLayout();
            this.grid = this.getGrid();
        }

        // determine column
        var col = 0, xs = this.grid.columnX, cmatch = false;
        for(var len = xs.length; col < len; col++){
            if(xy[0] < (xs[col].x + xs[col].w)){
                cmatch = true;
                break;
            }
        }
        // no match, fix last index
        if(!cmatch){
            col--;
        }

        // find insert position
        var p, match = false, pos = 0,
            c = portal.items.itemAt(col),
            items = c.items.items;

        for(var len = items.length; pos < len; pos++){
            p = items[pos];
            var h = p.el.getHeight();
            if(h !== 0 && (p.el.getY()+(h/2)) > xy[1]){
                match = true;
                break;
            }
        }

        var overEvent = this.createEvent(dd, e, data, col, c,
                match && p ? pos : c.items.getCount());

        if(portal.fireEvent('validatedrop', overEvent) !== false &&
           portal.fireEvent('beforedragover', overEvent) !== false){

            // make sure proxy width is fluid
            px.getProxy().setWidth('auto');

            if(p){
                px.moveProxy(p.el.dom.parentNode, match ? p.el.dom : null);
            }else{
                px.moveProxy(c.el.dom, null);
            }

            this.lastPos = {c: c, col: col, p: match && p ? pos : false};
            this.scrollPos = portal.body.getScroll();

            portal.fireEvent('dragover', overEvent);

            return overEvent.status;;
        }else{
            return overEvent.status;
        }

    },

    notifyOut : function(){
        delete this.grid;
    },

    notifyDrop : function(dd, e, data){
        delete this.grid;
        if(!this.lastPos){
            return;
        }
        var c = this.lastPos.c, col = this.lastPos.col, pos = this.lastPos.p;

        var dropEvent = this.createEvent(dd, e, data, col, c,
                pos !== false ? pos : c.items.getCount());

        if(this.portal.fireEvent('validatedrop', dropEvent) !== false &&
           this.portal.fireEvent('beforedrop', dropEvent) !== false){

            dd.proxy.getProxy().remove();
            dd.panel.el.dom.parentNode.removeChild(dd.panel.el.dom);
            if(pos !== false){
                c.insert(pos, dd.panel);
            }else{
                c.add(dd.panel);
            }
            
            c.doLayout();

            this.portal.fireEvent('drop', dropEvent);

            // scroll position is lost on drop, fix it
            var st = this.scrollPos.top;
            if(st){
                var d = this.portal.body.dom;
                setTimeout(function(){
                    d.scrollTop = st;
                }, 10);
            }

        }
        delete this.lastPos;
    },

    // internal cache of body and column coords
    getGrid : function(){
        var box = this.portal.bwrap.getBox();
        box.columnX = [];
        this.portal.items.each(function(c){
             box.columnX.push({x: c.el.getX(), w: c.el.getWidth()});
        });
        return box;
    }
});

/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.ux.PortalColumn = Ext.extend(Ext.Container, {
    layout: 'anchor',
    autoEl: 'div',
    defaultType: 'portlet',
    cls:'x-portal-column'
});
Ext.reg('portalcolumn', Ext.ux.PortalColumn);

/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.ux.Portlet = Ext.extend(Ext.Panel, {
    anchor: '100%',
    frame:true,
    collapsible:true,
    draggable:true,
    cls:'x-portlet'
});
Ext.reg('portlet', Ext.ux.Portlet);


Ext.namespace('Ext.ux.form');

Ext.ux.form.LovField = Ext.extend(Ext.form.TriggerField, {
    defaultAutoCreate : {tag: "input", type: "text", size: "16",style:"cursor:default;", autocomplete: "off"},
    triggerClass: 'x-form-search-trigger',
    validateOnBlur: false,

    // LOV window width
    lovWidth: 300,
    // LOV window height
    lovHeight: 300,
    // LOV window title
    lovTitle: '',
    // Multiple selection is possible?
    multiSelect: false,

    // If this option is true, data store reloads each time the LOV opens
    alwaysLoadStore: false,
    // LOV data provider, intance of Ext.grid.GridPanel or Ext.DataView
    view: {},

    // Which data store field will use for return
    valueField: 'id',
    // Which data store field will use for display
    displayField: 'id',
    // If multiple items are selected, they are joined with this character
    valueSeparator: ',',
    displaySeparator: ',',

    // LOV window configurations
    // autoScroll, layout, bbar and items configurations are not changed by this option
    windowConfig: {},

    showOnFocus : false,

    minItem : 0,
    minItemText : 'The minimum selected item number for this field is {0}',

    maxItem : Number.MAX_VALUE,
    maxItemText : 'The maximum selected item number for this field is {0}',

    // Private
    isStoreLoaded: false,
    // Private
    selections: [],
    // Private
    selectedRecords: [],

    initComponent: function(){
        if((this.view.xtype != 'grid' && this.view.xtype != 'dataview') &&
        (!(this.view instanceof Ext.grid.GridPanel) && !(this.view instanceof Ext.DataView))){
            throw "Ext.ux.form.LovField.view option must be instance of Ext.grid.GridPanel or Ext.DataView!";
        }

        Ext.ux.form.LovField.superclass.initComponent.call(this);

        this.viewType = this.view.getXType();
        if(this.viewType == 'grid' && !this.view.sm){
            this.view.sm = this.view.getSelectionModel();
        }

        if(this.viewType == 'grid'){
            this.view.sm.singleSelect = !this.multiSelect;
        }else{
            this.view.singleSelect = !this.multiSelect;
            this.view.multiSelect = this.multiSelect;
        }

        if(Ext.type(this.displayField) == 'array'){
            this.displayField = this.displayField.join('');
        }
        if (/<tpl(.*)<\/tpl>/.test(this.displayField) && !(this.displayFieldTpl instanceof Ext.XTemplate)) {
            this.displayFieldTpl = new Ext.XTemplate(this.displayField).compile();
        }

        if(Ext.type(this.qtipTpl) == 'array'){
            this.qtipTpl = this.qtipTpl.join('');
        }
        if(/<tpl(.*)<\/tpl>/.test(this.qtipTpl) && !(this.qtipTpl instanceof Ext.XTemplate) ){
            this.qtipTpl = new Ext.XTemplate(this.qtipTpl).compile();
        }

        // If store was auto loaded mark it as loaded
        if (this.view.store.autoLoad) {
            this.isStoreLoaded = true;
        }
    },

    onRender: function(ct, position){
        if (this.isRendered) {
            return;
        }

        this.readOnly = true;
        if(this.textarea){
            this.defaultAutoCreate = {tag: "textarea", style:"cursor:default;width:124px;height:65px;", autocomplete: "off"};
            this.displaySeparator = '\n';
        }

        Ext.ux.form.LovField.superclass.onRender.call(this, ct, position);
        if(this.text) this.el.dom.value = this.text;
        if(this.value && this.value.split) {
            var values = this.value.split(',');
            for(var i=0; i<values.length; i++){
                var hidden = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName}, 'before', true);
                hidden.setAttribute("value", values[i]);
            }
            var view = this.view;
            this.createWindow();
            var win = this.window;
            this.view.store.load({callback:function(){
                var records = new Array();
                for(var i=0; i<values.length; i++){
                    var r = this.getById(values[i]);
                    records.push(r);
                }
                win.on('show', function(){
                    view.sm.selectRecords(records);
                })
            }});
        }

//        this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden',

        // prevent input submission
        this.el.dom.removeAttribute('name');
    	if(this.width){
        	this.el.setWidth(this.width);
    	}

        if(this.showOnFocus){
            this.on('focus',this.onTriggerClick,this);
        }

        this.isRendered = true;
    },

    validateValue : function(value){
        if( Ext.ux.form.LovField.superclass.validateValue.call(this, value)){
            if(this.selectedRecords.length < this.minItem){
                this.markInvalid(String.format(this.minItemText, this.minItem));
                return false;
            }
            if(this.selectedRecords.length > this.maxItem){
                this.markInvalid(String.format(this.maxItemText, this.maxItem));
                return false;
            }
        }else{
            return false;
        }
        return true;
    },

    getSelectedRecords : function(){
        if(this.viewType == 'grid'){
            this.selections = this.selectedRecords = this.view.sm.getSelections();
        }else{
            this.selections = this.view.getSelectedIndexes();
            this.selectedRecords = this.view.getSelectedRecords();
        }

        return this.selectedRecords;
    },

    clearSelections : function(){
        return (this.viewType == 'grid')? this.view.sm.clearSelections() : this.view.clearSelections();
    },

    select : function(selections){
        if(this.viewType == 'grid'){
            if(selections[0] instanceof Ext.data.Record){
                this.view.sm.selectRecords(selections);
            }else{
                this.view.sm.selectRows(selections);

            }
        }else{
            this.view.select(selections);
        }
    },

    onSelect: function(){
        var d = this.prepareValue(this.getSelectedRecords());
        var returnValue = d.hv ? d.hv.join(this.valueSeparator) : '';
        var displayValue = d.dv ? d.dv.join(this.displaySeparator) : '';

        Ext.form.ComboBox.superclass.setValue.call(this, displayValue);
        if(d.hv){
            var form = this.el.findParent('.x-form', null, true);
            Ext.each(form.query('input[@name=' + this.hiddenName + ']'), function(e){
                Ext.get(e).remove();
            });

            var el = this.el;
            var hiddenName = this.hiddenName;
            Ext.each(d.hv, function(r){
                var hidden = el.insertSibling({tag:'input', type:'hidden', name: hiddenName}, 'before', true);
                hidden.setAttribute("value", r);
            });
        }else{
            var form = this.el.findParent('.x-form', null, true);
            Ext.each(form.query('input[@name=' + this.hiddenName + ']'), function(e){
                Ext.get(e).remove();
            });
        }

        //this.hiddenField.setAttribute('value', returnValue);

        if(Ext.QuickTips){ // fix for floating editors interacting with DND
            Ext.QuickTips.enable();
        }
        this.window.hide();
    },

    prepareValue:function(sRec){
        this.el.dom.qtip = '';
        if (sRec.length > 0) {
            var vals = {"hv": [],"dv": []};
            Ext.each(sRec, function(i){
                vals.hv.push(i.get(this.valueField));
                if (this.displayFieldTpl) {
                    vals.dv.push(this.displayFieldTpl.apply(i.data));
                } else {
                    vals.dv.push(i.get(this.displayField));
                }

                if(this.qtipTpl){
                    this.el.dom.qtip += this.qtipTpl.apply(i.data);
                }

            }, this);
            return vals;
        }
        return false;
    },

    getValue:function(){
        var v = '';// = this.hiddenField.value;
        var form = this.el.findParent('.x-form', null, true);
        Ext.each(form.query('input[@name=' + this.hiddenName + ']'), function(e){
            v += Ext.get(e).dom.value + ',';
        });
        v = v.substr(0, v.length - 1);
        if(v === this.emptyText || v === undefined){
            v = '';
        }
        return v;
    },

    setValue : function(v){
        this.value = v;
        if(this.rendered){
            //this.el.dom.value = (v === null || v === undefined ? '' : v); 
            var form = this.el.findParent('.x-form', null, true);
	        Ext.each(form.query('input[@name=' + this.hiddenName + ']'), function(e){
	            Ext.removeNode(Ext.get(e).dom);
	        });
            this.validate();
        }
    },

    onTriggerClick: function(e){
        // Store Load
        if (!this.isStoreLoaded) {
            this.view.store.load();
            this.isStoreLoaded = true;
        } else if (this.alwaysLoadStore === true) {
            this.view.store.reload();
        }

        this.createWindow();
        
        this.window.setPagePosition(e.xy[0] + 16, e.xy[1] + 16);

        this.window.show();
    },

    createWindow: function(){
        this.windowConfig = Ext.apply(this.windowConfig,
        {
            title: this.lovTitle,
            width: this.lovWidth,
            height: this.lovHeight,
            autoScroll: true,
            layout: 'fit',
            bbar: [
            {text: 'Cancel', handler: function(){
                this.select(this.selections);
                this.window.hide();
            },scope: this},
            {text: 'Clear', handler: function(){this.clearSelections();},scope: this},
            '->',
            {text: 'Select',handler: this.onSelect,scope: this}
            ],
            items: this.view
        },{shadow: false, frame: true});

        if(!this.window){
            this.window = new Ext.Window(this.windowConfig);

            this.window.on('beforeclose', function(){
                this.window.hide();
                return false;
            }, this);

            this.window.on('hide', this.validate, this);
            this.view.on('dblclick', this.onSelect, this);
        }
    }
});

Ext.reg('xlovfield', Ext.ux.form.LovField);

/** Normal grid with locking column support */
Ext.grid.LockingGridPanel = Ext.extend(Ext.grid.GridPanel,{
    getView : function(){
        if(!this.view){
            this.view = new Ext.grid.LockingGridView(this.viewConfig);
        }
        return this.view;
    },
    initComponent : function(){
		if(!this.cm) {
			this.cm = new Ext.grid.LockingColumnModel(this.columns);
			delete this.columns;
		}
        Ext.grid.LockingGridPanel.superclass.initComponent.call(this);
	}
});

/** Editor grid with locking column support */
Ext.grid.LockingEditorGridPanel = Ext.extend(Ext.grid.EditorGridPanel,{
    getView : function(){
        if(!this.view){
            this.view = new Ext.grid.LockingGridView(this.viewConfig);
        }
        return this.view;
    },
    initComponent : function(){
        if(!this.cm) {
            this.cm = new Ext.grid.LockingColumnModel(this.columns);
            delete this.columns;
        }
        Ext.grid.LockingEditorGridPanel.superclass.initComponent.call(this);
    }
});

Ext.grid.LockingGridView = function(config){
    Ext.apply(this, config);
	if(!this.templates) this.templates={};
	if(!this.templates.master){
		this.templates.master = new Ext.Template(
                    '<div class="x-grid3" hidefocus="true">',
						'<div class="x-grid3-locked">',
                            '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{lockedHeader}</div></div><div class="x-clear"></div></div>',
                            '<div class="x-grid3-scroller"><div class="x-grid3-body">{lockedBody}</div></div>',
				        '</div>',
                        '<div class="x-grid3-viewport">',
                            '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{header}</div></div><div class="x-clear"></div></div>',
                            '<div class="x-grid3-scroller"><div class="x-grid3-body">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
                        '</div>',
                        '<div class="x-grid3-resize-marker">&#160;</div>',
                        '<div class="x-grid3-resize-proxy">&#160;</div>',
                    '</div>'
                    );
	}
    Ext.grid.LockingGridView.superclass.constructor.call(this);
};

Ext.extend(Ext.grid.LockingGridView, Ext.grid.GridView, {

	lockText : "Lock",
	unlockText : "Unlock",

	//Template has changed and we need a few other pointers to keep track
	initElements : function(){
        var E = Ext.Element;

        var el = this.grid.getGridEl();
		el = el.dom.firstChild;//.dom.childNodes[1];
	    var cs = el.childNodes;

	    this.el = new E(el);

		this.lockedWrap = new E(cs[0]);
	    this.lockedHd = new E(this.lockedWrap.dom.firstChild);
	    this.lockedInnerHd = this.lockedHd.dom.firstChild;
        this.lockedScroller = new E(this.lockedWrap.dom.childNodes[1]);
        this.lockedBody = new E(this.lockedScroller.dom.firstChild);

        this.mainWrap = new E(cs[1]);
	    this.mainHd = new E(this.mainWrap.dom.firstChild);
	    this.innerHd = this.mainHd.dom.firstChild;
        this.scroller = new E(this.mainWrap.dom.childNodes[1]);
        if(this.forceFit){
            this.scroller.setStyle('overflow-x', 'hidden');
        }
        this.mainBody = new E(this.scroller.dom.firstChild);

	    this.focusEl = new E(this.scroller.dom.childNodes[1]);
        this.focusEl.swallowEvent("click", true);

        this.resizeMarker = new E(cs[2]);
        this.resizeProxy = new E(cs[3]);
    },
	
    getLockedRows : function(){
        return this.hasRows() ? this.lockedBody.dom.childNodes : [];
    },

    getLockedRow : function(row){
        return this.getLockedRows()[row];
    },

    getCell : function(rowIndex, colIndex){
	    var locked = this.cm.getLockedCount();
	    var row;
	    if(colIndex < locked){
	        row = this.getLockedRow(rowIndex);
	    }else{
	        row = this.getRow(rowIndex);
	        colIndex -= locked;
	    }
        return row.getElementsByTagName('td')[colIndex];
	},


    getHeaderCell : function(index){
	    var locked = this.cm.getLockedCount();
	    if(index < locked){
			return this.lockedHd.dom.getElementsByTagName('td')[index];
		} else {
			return this.mainHd.dom.getElementsByTagName('td')[(index-locked)];
		}
	},	
	
    scrollToTop : function(){
		Ext.grid.LockingGridView.superclass.scrollToTop.call(this);
		this.syncScroll();
    },

    syncScroll : function(e){
		Ext.grid.LockingGridView.superclass.syncScroll.call(this, e);
        var mb = this.scroller.dom;
		this.lockedScroller.dom.scrollTop = mb.scrollTop;
    },

    processRows : function(startRow, skipStripe){
        if(this.ds.getCount() < 1){
            return;
        }
        skipStripe = skipStripe || !this.grid.stripeRows;
        startRow = startRow || 0;
        var cls = ' x-grid3-row-alt ';
        var rows = this.getRows();
        var lrows = this.getLockedRows();
        for(var i = startRow, len = rows.length; i < len; i++){
            var row = rows[i];
            var lrow = lrows[i];
            row.rowIndex = i;
            lrow.rowIndex = i;
            if(!skipStripe){
                var isAlt = ((i+1) % 2 == 0);
                var hasAlt = (' '+row.className + ' ').indexOf(cls) != -1;
                if(isAlt == hasAlt){
                    continue;
                }
                if(isAlt){
                    row.className += " x-grid3-row-alt";
                    lrow.className += " x-grid3-row-alt";
                }else{
                    row.className = row.className.replace("x-grid3-row-alt", "");
                    lrow.className = lrow.className.replace("x-grid3-row-alt", "");
                }
            }
        }
    },

    updateSortIcon : function(col, dir){
        var sc = this.sortClasses;
        var clen = this.cm.getColumnCount();
		var lclen = this.cm.getLockedCount();
        var hds = this.mainHd.select('td').removeClass(sc);
        var lhds = this.lockedHd.select('td').removeClass(sc);
		if(lclen > 0 && col < lclen) 
			lhds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
		else
			hds.item(col-lclen).addClass(sc[dir == "DESC" ? 1 : 0]);
    },
	
	//only used for forceFit?
    updateAllColumnWidths : function(){
		
        var tw = this.cm.getTotalWidth();
		var lw = this.cm.getTotalLockedWidth();
        var clen = this.cm.getColumnCount();
		var lclen = this.cm.getLockedCount();
        var ws = [];
        for(var i = 0; i < clen; i++){
            ws[i] = this.getColumnWidth(i);
        }

        this.innerHd.firstChild.firstChild.style.width = (tw - lw) + 'px';
		this.mainWrap.dom.style.left = lw + 'px';
		this.lockedInnerHd.firstChild.firstChild.style.width = lw + 'px';

        for(var i = 0; i < clen; i++){
            var hd = this.getHeaderCell(i);
            hd.style.width = ws[i] + 'px';
        }

        var ns = this.getRows();
		var lns = this.getLockedRows();
        for(var i = 0, len = ns.length; i < len; i++){
            ns[i].style.width =(tw - lw) + 'px';
            ns[i].firstChild.style.width = (tw-lw) + 'px';
            lns[i].style.width = lw + 'px';
            lns[i].firstChild.style.width = lw + 'px';
            for(var j = 0; j < lclen; j++){
				var row = lns[i].firstChild.rows[0];
                row.childNodes[j].style.width = ws[j] + 'px';
            }
            for(var j = lclen; j < clen; j++){
				var row = ns[i].firstChild.rows[0];
                row.childNodes[j].style.width = ws[j] + 'px';
            }
        }

        this.onAllColumnWidthsUpdated(ws, tw);
    },

    updateColumnWidth : function(col, width){
        var w = this.cm.getColumnWidth(col);
        var tw = this.cm.getTotalWidth();
		var lclen = this.cm.getLockedCount();
		var lw = this.cm.getTotalLockedWidth();

        var hd = this.getHeaderCell(col);
        hd.style.width = w + 'px';

		var ns, gw;
		if(col < lclen) {
			ns = this.getLockedRows();
			gw = lw;
			this.lockedInnerHd.firstChild.firstChild.style.width = gw + 'px';
			this.mainWrap.dom.style.left= this.cm.getTotalLockedWidth() + 'px';
			this.mainWrap.dom.style.display='none';
			this.mainWrap.dom.style.display='';
		}else {
			ns = this.getRows();
			gw = tw - lw;
			col -= lclen;
			this.innerHd.firstChild.firstChild.style.width = gw + 'px';
		}
		
        for(var i = 0, len = ns.length; i < len; i++){
            ns[i].style.width = gw + 'px';
            ns[i].firstChild.style.width = gw + 'px';
            ns[i].firstChild.rows[0].childNodes[col].style.width = w + 'px';
        }

        this.onColumnWidthUpdated(col, w, tw);
		this.layout();
    },

    updateColumnHidden : function(col, hidden){
        var tw = this.cm.getTotalWidth();
		var lw = this.cm.getTotalLockedWidth();
		var lclen = this.cm.getLockedCount();

        this.innerHd.firstChild.firstChild.style.width = tw + 'px';

        var display = hidden ? 'none' : '';

        var hd = this.getHeaderCell(col);
        hd.style.display = display;

        var ns, gw;
		if(col < lclen) {
			ns = this.getLockedRows();
			gw = lw;
			this.lockedHd.dom.firstChild.firstChild.style.width = gw + 'px';
			this.mainWrap.dom.style.left= this.cm.getTotalLockedWidth() + 'px';
		} else {
			ns = this.getRows();
			gw = tw - lw;
			col -= lclen;
			this.innerHd.firstChild.firstChild.style.width = gw + 'px';
		}

        for(var i = 0, len = ns.length; i < len; i++){
            ns[i].style.width = gw + 'px';
            ns[i].firstChild.style.width = gw + 'px';
            ns[i].firstChild.rows[0].childNodes[col].style.display = display;
        }

        this.onColumnHiddenUpdated(col, hidden, tw);

        delete this.lastViewWidth;         
		this.layout();
    },

   syncHeaderHeight : function() {
        if (this.lockedInnerHd == undefined || this.innerHd == undefined) return;

        this.lockedInnerHd.firstChild.firstChild.style.height = "auto";
        this.innerHd.firstChild.firstChild.style.height = "auto";
        var height = (this.lockedInnerHd.firstChild.firstChild.offsetHeight > this.innerHd.firstChild.firstChild.offsetHeight) ? 
                          this.lockedInnerHd.firstChild.firstChild.offsetHeight : this.innerHd.firstChild.firstChild.offsetHeight;
        this.lockedInnerHd.firstChild.firstChild.style.height = height + 'px';
        this.innerHd.firstChild.firstChild.style.height = height + 'px';

        //if (this.lockedScroller == undefined || this.scroller == undefined) return;
        //this.lockedScroller.dom.style.height = (this.el.dom.offsetHeight - height) + 'px';
        //this.scroller.dom.style.height = (this.el.dom.offsetHeight - height) + 'px';
    },
	
    doRender : function(cs, rs, ds, startRow, colCount, stripe){
        var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount-1;
        var tw = this.cm.getTotalWidth();
		var lw = this.cm.getTotalLockedWidth();
		var clen = this.cm.getColumnCount();
		var lclen = this.cm.getLockedCount();
        var tstyle = 'width:'+this.getTotalWidth()+';';
                var buf = [], lbuf = [], cb, lcb, c, p = {}, rp = {tstyle: tstyle}, r;
        for(var j = 0, len = rs.length; j < len; j++){
            r = rs[j]; cb = []; lcb = [];
            var rowIndex = (j+startRow);
            for(var i = 0; i < colCount; i++){
                c = cs[i];
                p.id = c.id;
                p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                p.attr = p.cellAttr = "";
                p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
                p.style = c.style;
                if(p.value == undefined || p.value === "") p.value = "&#160;";
                if(r.dirty && typeof r.modified[c.name] !== 'undefined'){
                    p.css += ' x-grid3-dirty-cell';
                }
				if(c.locked)
					lcb[lcb.length] = ct.apply(p);
				else
					cb[cb.length] = ct.apply(p);
            }
            var alt = [];
            if(stripe && ((rowIndex+1) % 2 == 0)){
                alt[0] = "x-grid3-row-alt";
            }
            if(r.dirty){
                alt[1] = " x-grid3-dirty-row";
            }
            rp.cols = colCount;
            if(this.getRowClass){
                alt[2] = this.getRowClass(r, rowIndex, rp, ds);
            }
			
            rp.alt = alt.join(" ");
            rp.cells = lcb.join("");
			rp.tstyle = 'width:'+lw+'px;';
            lbuf[lbuf.length] =  rt.apply(rp);

            rp.cells = cb.join("");
			rp.tstyle = 'width:'+(tw-lw)+'px;';
            buf[buf.length] =  rt.apply(rp);
        }
        return [buf.join(""), lbuf.join("")];
    },

    renderUI : function(){

        var header = this.renderHeaders();
        var body = this.templates.body.apply({rows:''});


        var html = this.templates.master.apply({
            body: body,
            header: header[0],
			lockedBody: body,
            lockedHeader: header[1]
        });

        var g = this.grid;

        g.getGridEl().dom.innerHTML = html;

        this.initElements();

		var bd = this.renderRows();
        if (bd == '') bd = ['', ''];
    
        this.mainBody.dom.innerHTML = bd[0];
        this.lockedBody.dom.innerHTML = bd[1];
        this.processRows(0, true);


        Ext.fly(this.innerHd).on("click", this.handleHdDown, this);
        Ext.fly(this.lockedInnerHd).on("click", this.handleHdDown, this);
        this.mainHd.on("mouseover", this.handleHdOver, this);
        this.mainHd.on("mouseout", this.handleHdOut, this);
        this.mainHd.on("mousemove", this.handleHdMove, this);
        this.lockedHd.on("mouseover", this.handleHdOver, this);
        this.lockedHd.on("mouseout", this.handleHdOut, this);
        this.lockedHd.on("mousemove", this.handleHdMove, this);
		this.mainWrap.dom.style.left= this.cm.getTotalLockedWidth() + 'px';
        this.scroller.on('scroll', this.syncScroll,  this);
        if(g.enableColumnResize !== false){
            this.splitone = new Ext.grid.GridView.SplitDragZone(g, this.lockedHd.dom);
			this.splitone.setOuterHandleElId(Ext.id(this.lockedHd.dom));
			this.splitone.setOuterHandleElId(Ext.id(this.mainHd.dom));
        }

        if(g.enableColumnMove){
            this.columnDrag = new Ext.grid.GridView.ColumnDragZone(g, this.innerHd);
            this.columnDrop = new Ext.grid.HeaderDropZone(g, this.mainHd.dom);
        }

        if(g.enableHdMenu !== false){
            if(g.enableColumnHide !== false){
                this.colMenu = new Ext.menu.Menu({id:g.id + "-hcols-menu"});
                this.colMenu.on("beforeshow", this.beforeColMenuShow, this);
                this.colMenu.on("itemclick", this.handleHdMenuClick, this);
            }
            this.hmenu = new Ext.menu.Menu({id: g.id + "-hctx"});
            this.hmenu.add(
                {id:"asc", text: this.sortAscText, cls: "xg-hmenu-sort-asc"},
                {id:"desc", text: this.sortDescText, cls: "xg-hmenu-sort-desc"}
            );
            if(this.grid.enableColLock !== false){
                this.hmenu.add('-',
                    {id:"lock", text: this.lockText, cls: "xg-hmenu-lock"},
                    {id:"unlock", text: this.unlockText, cls: "xg-hmenu-unlock"}
                );
            }
			
            if(g.enableColumnHide !== false){
                this.hmenu.add('-',
                    {id:"columns", text: this.columnsText, menu: this.colMenu, iconCls: 'x-cols-icon'}
                );
            }
            this.hmenu.on("itemclick", this.handleHdMenuClick, this);

        }

        if(g.enableDragDrop || g.enableDrag){
            var dd = new Ext.grid.GridDragZone(g, {
                ddGroup : g.ddGroup || 'GridDD'
            });
        }

        this.updateHeaderSortState();

    },

    layout : function(){
        if(!this.mainBody){
            return;
        }
        var g = this.grid;
        var c = g.getGridEl(), cm = this.cm,
                expandCol = g.autoExpandColumn,
                gv = this;
		var lw = cm.getTotalLockedWidth();
        var csize = c.getSize(true);
        var vw = csize.width;

        if (vw < 20 || csize.height < 20) {
            return;
        }

		this.syncHeaderHeight();
        if(g.autoHeight){
            this.scroller.dom.style.overflow = 'visible';
            this.lockedScroller.dom.style.overflow = 'visible';
        }else{
            this.el.setSize(csize.width, csize.height);

            var hdHeight = this.mainHd.getHeight();
            var vh = csize.height - (hdHeight);
			
            this.scroller.setSize(vw- lw, vh);
			var scrollbar = (this.scroller.dom.scrollWidth > this.scroller.dom.clientWidth)?17:0;
            this.lockedScroller.setSize(cm.getTotalLockedWidth(), vh-scrollbar);
            if(this.innerHd){
                this.innerHd.style.width = (vw)+'px';
            }
        }
        if(this.forceFit){
            if(this.lastViewWidth != vw){
                this.fitColumns(false, false);
                this.lastViewWidth = vw;
            }
        }else {
            this.autoExpand();
			lw = cm.getTotalLockedWidth(); //recalculate, as it might be changed by autoExpand
        }
		this.mainWrap.dom.style.left = lw +'px';
        this.onLayout(vw, vh);
    },

    renderHeaders : function(){
	    var cm = this.cm, ts = this.templates;
        var ct = ts.hcell;
        var tw = this.cm.getTotalWidth();
		var lw = this.cm.getTotalLockedWidth();

        var cb = [], lb = [], sb = [], lsb = [], p = {};

        for(var i = 0, len = cm.getColumnCount(); i < len; i++){
            p.id = cm.getColumnId(i);
            p.value = cm.getColumnHeader(i) || "";
            p.style = this.getColumnStyle(i, true);
            p.tooltip = this.getColumnTooltip(i);
            
            if(cm.config[i].align == 'right'){
                p.istyle = 'padding-right:16px';
            }
			if(cm.isLocked(i)) {
				lb[lb.length] = ct.apply(p);
			} else {
				cb[cb.length] = ct.apply(p);
			}
        }
        return [ts.header.apply({cells: cb.join(""), tstyle:'width:'+(tw-lw)+';'}),
				ts.header.apply({cells: lb.join(""), tstyle:'width:'+(lw)+';'})];
	},

    // private
    getColumnTooltip : function(i) {
        var tt = this.cm.getColumnTooltip(i);
        if (tt) {
            if (Ext.QuickTips.isEnabled()) {
                return 'ext:qtip="'+tt+'"';
            } else {
                return 'title="'+tt+'"';
            }
        }
        return "";
    },

    updateHeaders : function(){
        var hd = this.renderHeaders();
		this.innerHd.firstChild.innerHTML = hd[0];
		this.lockedInnerHd.firstChild.innerHTML = hd[1];
    },

    insertRows : function(dm, firstRow, lastRow, isUpdate){
        if(firstRow === 0 && lastRow == dm.getCount()-1){
            this.refresh();
        }else{
            if(!isUpdate){
                this.fireEvent("beforerowsinserted", this, firstRow, lastRow);
            }
            var html = this.renderRows(firstRow, lastRow);
            var before = this.getRow(firstRow);
			var beforeLocked = this.getLockedRow(firstRow);
            if(before){
                Ext.DomHelper.insertHtml('beforeBegin', before, html[0]);
            } else {
                Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html[0]);
            }
      
            var beforeLocked = this.getLockedRow(firstRow);
            if (beforeLocked) {
                Ext.DomHelper.insertHtml('beforeBegin', beforeLocked, html[1]);
            }else{
                Ext.DomHelper.insertHtml('beforeEnd', this.lockedBody.dom, html[1]);
            }
            if(!isUpdate){
                this.fireEvent("rowsinserted", this, firstRow, lastRow);
                this.processRows(firstRow);
            }
        }
    },
    // private
    removeRow : function(row) {
        Ext.removeNode(this.getRow(row));
        if (this.cm.getLockedCount() > 0) {
            Ext.removeNode(this.getLockedRow(row));
        }
    },

    getColumnData : function(){
                var cs = [], cm = this.cm, colCount = cm.getColumnCount();
        for(var i = 0; i < colCount; i++){
            var name = cm.getDataIndex(i);
            cs[i] = {
                name : (typeof name == 'undefined' ? this.ds.fields.get(i).name : name),
                renderer : cm.getRenderer(i),
                id : cm.getColumnId(i),
                style : this.getColumnStyle(i),
				locked : cm.isLocked(i)
            };
        }
        return cs;
    },

    renderBody : function(){
        var markup = this.renderRows();
        return [this.templates.body.apply({rows: markup[0]}), this.templates.body.apply({rows: markup[1]})];
    },

    refresh : function(headersToo){
        this.fireEvent("beforerefresh", this);
        this.grid.stopEditing();

        var result = this.renderBody();
        this.mainBody.update(result[0]);
        this.lockedBody.update(result[1]);

        if(headersToo === true){
            this.updateHeaders();
            this.updateHeaderSortState();
        }
        this.processRows(0, true);
        this.layout();
        this.applyEmptyText();
        this.fireEvent("refresh", this);
    },

    handleLockChange : function(){
        this.refresh(true);
    },

    onDenyColumnHide : function(){

    },

    onColumnLock : function(){
        this.handleLockChange.apply(this, arguments);
    },

	addRowClass : function(row, cls){
		var r = this.getRow(row);
		if(r){
			this.fly(r).addClass(cls);
			r = this.getLockedRow(row);
			this.fly(r).addClass(cls);
		}
	},

	removeRowClass : function(row, cls){
		var r = this.getRow(row);
		if(r){
			this.fly(r).removeClass(cls);
			r = this.getLockedRow(row);
			this.fly(r).removeClass(cls);
		}
	},
	
    handleHdMenuClick : function(item){
        var index = this.hdCtxIndex;
        var cm = this.cm, ds = this.ds;
        switch(item.id){
            case "asc":
                ds.sort(cm.getDataIndex(index), "ASC");
                break;
            case "desc":
                ds.sort(cm.getDataIndex(index), "DESC");
                break;
            case "lock":
                var lc = cm.getLockedCount();
                if(cm.getColumnCount(true) <= lc+1){
                    this.onDenyColumnLock();
                    return;
                }
                if(lc != index){
                    cm.setLocked(index, true, true);
                    cm.moveColumn(index, lc);
                    this.grid.fireEvent("columnmove", index, lc);
                }else{
                    cm.setLocked(index, true);
                }
            break;
            case "unlock":
                var lc = cm.getLockedCount();
                if((lc-1) != index){
                    cm.setLocked(index, false, true);
                    cm.moveColumn(index, lc-1);
                    this.grid.fireEvent("columnmove", index, lc-1);
                }else{
                    cm.setLocked(index, false);
                }
            break;
            default:
                index = cm.getIndexById(item.id.substr(4));
                if(index != -1){
                    if(item.checked && cm.getColumnsBy(this.isHideableColumn, this).length <= 1){
                        this.onDenyColumnHide();
                        return false;
                    }
                    cm.setHidden(index, item.checked);
                }
        }
        return true;
    },

    handleHdDown : function(e, t){
        if(Ext.fly(t).hasClass('x-grid3-hd-btn')){
            e.stopEvent();
            var hd = this.findHeaderCell(t);
            Ext.fly(hd).addClass('x-grid3-hd-menu-open');
            var index = this.getCellIndex(hd);
            this.hdCtxIndex = index;
            var ms = this.hmenu.items, cm = this.cm;
            ms.get("asc").setDisabled(!cm.isSortable(index));
            ms.get("desc").setDisabled(!cm.isSortable(index));
	        if(this.grid.enableColLock !== false){
	            ms.get("lock").setDisabled(cm.isLocked(index));
	            ms.get("unlock").setDisabled(!cm.isLocked(index));
	        }
            this.hmenu.on("hide", function(){
                Ext.fly(hd).removeClass('x-grid3-hd-menu-open');
            }, this, {single:true});
            this.hmenu.show(t, "tl-bl?");
        }
    }
});

Ext.grid.LockingColumnModel = function(config){
	Ext.grid.LockingColumnModel.superclass.constructor.call(this, config);
};

Ext.extend(Ext.grid.LockingColumnModel, Ext.grid.ColumnModel, {
    getTotalLockedWidth : function(){
        var totalWidth = 0;
        for(var i = 0; i < this.config.length; i++){
            if(this.isLocked(i) && !this.isHidden(i)){
                totalWidth += this.getColumnWidth(i);
            }
        }
        return totalWidth;
    }
});

