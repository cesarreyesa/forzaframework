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


// We are adding these custom layouts to a namespace that does not
// exist by default in Ext, so we have to add the namespace first:
Ext.ns('Ext.ux.layout');

/*
 * ================  RowLayout  =======================
 */
/**
 * @class Ext.ux.layout.RowLayout
 * @extends Ext.layout.ContainerLayout
 * <p>This is the layout style of choice for creating structural layouts in a multi-row format where the height of
 * each row can be specified as a percentage or fixed height.  Row widths can also be fixed, percentage or auto.
 * This class is intended to be extended or created via the layout:'ux.row' {@link Ext.Container#layout} config,
 * and should generally not need to be created directly via the new keyword.</p>
 * <p>RowLayout does not have any direct config options (other than inherited ones), but it does support a
 * specific config property of <b><tt>rowHeight</tt></b> that can be included in the config of any panel added to it.  The
 * layout will use the rowHeight (if present) or height of each panel during layout to determine how to size each panel.
 * If height or rowHeight is not specified for a given panel, its height will default to the panel's height (or auto).</p>
 * <p>The height property is always evaluated as pixels, and must be a number greater than or equal to 1.
 * The rowHeight property is always evaluated as a percentage, and must be a decimal value greater than 0 and
 * less than 1 (e.g., .25).</p>
 * <p>The basic rules for specifying row heights are pretty simple.  The logic makes two passes through the
 * set of contained panels.  During the first layout pass, all panels that either have a fixed height or none
 * specified (auto) are skipped, but their heights are subtracted from the overall container height.  During the second
 * pass, all panels with rowHeights are assigned pixel heights in proportion to their percentages based on
 * the total <b>remaining</b> container height.  In other words, percentage height panels are designed to fill the space
 * left over by all the fixed-height and/or auto-height panels.  Because of this, while you can specify any number of rows
 * with different percentages, the rowHeights must always add up to 1 (or 100%) when added together, otherwise your
 * layout may not render as expected.  Example usage:</p>
 * <pre><code>
// All rows are percentages -- they must add up to 1
var p = new Ext.Panel({
    title: 'Row Layout - Percentage Only',
    layout:'ux.row',
    items: [{
        title: 'Row 1',
        rowHeight: .25
    },{
        title: 'Row 2',
        rowHeight: .6
    },{
        title: 'Row 3',
        rowHeight: .15
    }]
});

// Mix of height and rowHeight -- all rowHeight values must add
// up to 1. The first row will take up exactly 120px, and the last two
// rows will fill the remaining container height.
var p = new Ext.Panel({
    title: 'Row Layout - Mixed',
    layout:'ux.row',
    items: [{
        title: 'Row 1',
        height: 120,
        // standard panel widths are still supported too:
        width: '50%' // or 200
    },{
        title: 'Row 2',
        rowHeight: .8,
        width: 300
    },{
        title: 'Row 3',
        rowHeight: .2
    }]
});
</code></pre>
 */
Ext.ux.layout.RowLayout = Ext.extend(Ext.layout.ContainerLayout, {
    // private
    monitorResize:true,

    // private
    isValidParent : function(c, target){
        return c.getEl().dom.parentNode == this.innerCt.dom;
    },

    // private
    onLayout : function(ct, target){
        var rs = ct.items.items, len = rs.length, r, i;

        if(!this.innerCt){
            target.addClass('ux-row-layout-ct');
            this.innerCt = target.createChild({cls:'x-row-inner'});
        }
        this.renderAll(ct, this.innerCt);

        var size = target.getViewSize();

        if(size.width < 1 && size.height < 1){ // display none?
            return;
        }

        var h = size.height - target.getPadding('tb'),
            ph = h;

        this.innerCt.setSize({height:h});

        // some rows can be percentages while others are fixed
        // so we need to make 2 passes

        for(i = 0; i < len; i++){
            r = rs[i];
            if(!r.rowHeight){
                ph -= (r.getSize().height + r.getEl().getMargins('tb'));
            }
        }

        ph = ph < 0 ? 0 : ph;

        for(i = 0; i < len; i++){
            r = rs[i];
            if(r.rowHeight){
                r.setSize({height: Math.floor(r.rowHeight*ph) - r.getEl().getMargins('tb')});
            }
        }
    }

    /**
     * @property activeItem
     * @hide
     */
});
Ext.Container.LAYOUTS['ux.row'] = Ext.ux.layout.RowLayout;

Ext.Toolbar.Statusbar = Ext.extend(Ext.Toolbar.TextItem, {
    cls : 'x-statusbar',
    /**
     * @cfg {String} busyIconCls
     * The default {@link #iconCls} applied when calling {@link #showBusy} (defaults to 'x-status-busy'). It can be
     * overridden at any time by passing the <tt>iconCls</tt> argument into <tt>showBusy</tt>. See the
     * iconCls docs for additional details about customizing the icon.
     */
    busyIconCls : 'x-status-busy',
    /**
     * @cfg {String} busyText
     * The default {@link #text} applied when calling {@link #showBusy} (defaults to 'Loading...'). It can be
     * overridden at any time by passing the <tt>text</tt> argument into <tt>showBusy</tt>.
     */
    busyText : 'Cargando...',
    /**
     * @cfg {Number} autoClear
     * The number of milliseconds to wait after setting the status via {@link #setStatus} before automatically
     * clearing the status text and icon (defaults to 5000).  Note that this only applies when passing the
     * <tt>clear</tt> argument to setStatus since that is the only way to defer clearing the status.  This can
     * be overridden by specifying a different <tt>wait</tt> value in setStatus. Calls to {@link #clearStatus}
     * always clear the status bar immediately and ignore this value.
     */
    autoClear : 4000,
    defaultText: '',
    defaultClear:true,

    // private
    activeThreadId : 0,

    setStatus : function(o){
        o = o || {};

        if(typeof o == 'string'){
            o = {text:o};
        }
        if(o.text !== undefined){
            this.setText(o.text);
        }
        if(o.iconCls !== undefined){
            this.setIcon(o.iconCls);
        }
        if(!o.clear){
        	if(typeof o.clear != 'boolean' && this.defaultClear && this.text != ''){
	        	o.clear = {};
        	}
        }
        if(o.clear){
            var c = o.clear || {},
                wait = this.autoClear,
                defaults = {useDefaults: true, anim: true};

            if(typeof c == 'object'){
                c = Ext.applyIf(c, defaults);
                if(c.wait){
                    wait = c.wait;
                }
            }else if(typeof c == 'number'){
                wait = c;
                c = defaults;
            }else if(typeof c == 'boolean'){
                c = defaults;
            }

            c.threadId = this.activeThreadId;
            this.clearStatus.defer(wait, this, [c]);
        }
        return this;
    },

    setSuccess : function(o){
    	o = o || {};
        if(typeof o == 'string'){
            o = {text:o};
        }
    	o.iconCls = 'x-status-valid';
    	this.setStatus(o);
    },

    setFailure : function(o){
    	o = o || {};
        if(typeof o == 'string'){
            o = {text:o};
        }
    	o.iconCls = 'x-status-error';
    	this.setStatus(o);
    },

	clearStatus: function(o){
        o = o || {};

        if(o.threadId && o.threadId !== this.activeThreadId){
            // this means the current call was made internally, but a newer
            // thread has set a message since this call was deferred.  Since
            // we don't want to overwrite a newer message just ignore.
            return this;
        }

        var text = o.useDefaults ? this.defaultText : '',
            iconCls = o.useDefaults ? (this.defaultIconCls ? this.defaultIconCls : '') : '';
        if(o.anim){
            this.getEl().fadeOut({
                remove: false,
                useDisplay: true,
                scope: this,
                callback: function(){
                    this.setStatus({
	                    text: text,
	                    iconCls: iconCls
	                });
                    this.getEl().show();
                }
            });
        }else{
            // hide/show the el to avoid jumpy text or icon
            this.getEl().hide();
	        this.setStatus({
	            text: text,
	            iconCls: iconCls
	        });
            this.getEl().show();
        }
        return this;
	},
	showBusy : function(o){
        if(typeof o == 'string'){
            o = {text:o};
        }
        o = Ext.applyIf(o || {}, {
            text: this.busyText,
            iconCls: this.busyIconCls,
            clear:false
        });
        return this.setStatus(o);
	},
    /**
     * Convenience method for setting the status text directly.  For more flexible options see {@link #setStatus}.
     * @param {String} text (optional) The text to set (defaults to '')
     * @return {Ext.StatusBar} this
     */
    setText : function(text){
        this.activeThreadId++;
        this.text = text || '';
        if(this.rendered){
            this.getEl().update(this.text);
        }
        return this;
    },

    /**
     * Returns the current status text.
     * @return {String} The status text
     */
    getText : function(){
        return this.text;
    },

    /**
     * Convenience method for setting the status icon directly.  For more flexible options see {@link #setStatus}.
     * See {@link #iconCls} for complete details about customizing the icon.
     * @param {String} iconCls (optional) The icon class to set (defaults to '', and any current icon class is removed)
     * @return {Ext.StatusBar} this
     */
    setIcon : function(cls){
        this.activeThreadId++;
        cls = cls || '';

        if(this.rendered){
	        if(this.currIconCls){
	            this.getEl().removeClass(this.currIconCls);
	            this.currIconCls = null;
	        }
	        if(cls.length > 0){
	            this.getEl().addClass(cls);
	            this.currIconCls = cls;
	        }
        }else{
            this.currIconCls = cls;
        }
        return this;
    }
});

Ext.reg('tbstatusbar', Ext.Toolbar.Statusbar);


Ext.ux.FileField = Ext.extend(Ext.form.TextField, {
    defaultAutoCreate : { tag: "input", type:'file', autocomplete: "off"}
});

Ext.reg('filefield', Ext.ux.FileField);

// funciones para esconder todo el field con todo y etiquetas
Ext.override(Ext.form.Field, {

	// le agrega el div para poner una descripcio
    onRender : function(ct, position){
        if(!this.el){
            var cfg = this.getAutoCreate();

            if(!cfg.name){
                cfg.name = this.name || this.id;
            }
            if(this.inputType){
                cfg.type = this.inputType;
            }
            this.autoEl = cfg;
        }
        Ext.form.Field.superclass.onRender.call(this, ct, position);

        var type = this.el.dom.type;
        if(type){
            if(type == 'password'){
                type = 'text';
            }
            this.el.addClass('x-form-'+type);
        }
        if(this.readOnly){
            this.el.dom.readOnly = true;
        }
        if(this.tabIndex !== undefined){
            this.el.dom.setAttribute('tabIndex', this.tabIndex);
        }

        this.el.addClass([this.fieldClass, this.cls]);
        // le agrega el div para poner una descripcio
        if(this.description && this.description.length > 0){
            ct.createChild({tag:'div', cls:'item-desc', html:this.description});
        }

    },
//	onRender : function(ct, position){
//	    Ext.form.Field.superclass.onRender.call(this, ct, position);
//	    if(!this.el){
//	        var cfg = this.getAutoCreate();
//	        if(!cfg.name){
//	            cfg.name = this.name || this.id;
//	        }
//	        if(this.inputType){
//	            cfg.type = this.inputType;
//	        }
//	        this.el = ct.createChild(cfg, position);
//	    }
//	    var type = this.el.dom.type;
//	    if(type){
//	        if(type == 'password'){
//	            type = 'text';
//	        }
//	        this.el.addClass('x-form-'+type);
//	    }
//	    if(this.readOnly){
//	        this.el.dom.readOnly = true;
//	    }
//	    if(this.tabIndex !== undefined){
//	        this.el.dom.setAttribute('tabIndex', this.tabIndex);
//	    }
//
//	    this.el.addClass([this.fieldClass, this.cls]);
//	},

    showContainer: function() {
        this.enable();
        this.show();
        this.getEl().up('.x-form-item').setDisplayed(true); // show entire container and children (including label if applicable)
    },

    hideContainer: function() {
        this.disable(); // for validation
        this.hide();
        this.getEl().up('.x-form-item').setDisplayed(false); // hide container and children (including label if applicable)
    },

    setContainerVisible: function(visible) {
        if (visible) {
            this.showContainer();
        } else {
            this.hideContainer();
        }
        return this;
    }
});

/*!
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ux.Portal = Ext.extend(Ext.Panel, {
    layout : 'column',
    autoScroll : true,
    cls : 'x-portal',
    defaultType : 'portalcolumn',

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
    },

    beforeDestroy : function() {
        if(this.dd){
            this.dd.unreg();
        }
        Ext.ux.Portal.superclass.beforeDestroy.call(this);
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
            items = c.items.items, overSelf = false;

        for(var len = items.length; pos < len; pos++){
            p = items[pos];
            var h = p.el.getHeight();
            if(h === 0){
                overSelf = true;
            }
            else if((p.el.getY()+(h/2)) > xy[1]){
                match = true;
                break;
            }
        }

        pos = (match && p ? pos : c.items.getCount()) + (overSelf ? -1 : 0);
        var overEvent = this.createEvent(dd, e, data, col, c, pos);

        if(portal.fireEvent('validatedrop', overEvent) !== false &&
           portal.fireEvent('beforedragover', overEvent) !== false){

            // make sure proxy width is fluid
            px.getProxy().setWidth('auto');

            if(p){
                px.moveProxy(p.el.dom.parentNode, match ? p.el.dom : null);
            }else{
                px.moveProxy(c.el.dom, null);
            }

            this.lastPos = {c: c, col: col, p: overSelf || (match && p) ? pos : false};
            this.scrollPos = portal.body.getScroll();

            portal.fireEvent('dragover', overEvent);

            return overEvent.status;
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
                if(c == dd.panel.ownerCt && (c.items.items.indexOf(dd.panel) <= pos)){
                    pos++;
                }
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
    },

    // unregister the dropzone from ScrollManager
    unreg: function() {
        //Ext.dd.ScrollManager.unregister(this.portal.body);
        Ext.ux.Portal.DropZone.superclass.unreg.call(this);
    }
});

/*!
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ux.PortalColumn = Ext.extend(Ext.Container, {
    layout : 'anchor',
    //autoEl : 'div',//already defined by Ext.Component
    defaultType : 'portlet',
    cls : 'x-portal-column'
});

Ext.reg('portalcolumn', Ext.ux.PortalColumn);

/*!
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ux.Portlet = Ext.extend(Ext.Panel, {
    anchor : '100%',
    frame : true,
    collapsible : true,
    draggable : true,
    cls : 'x-portlet'
});

Ext.reg('portlet', Ext.ux.Portlet);


Ext.namespace('Ext.ux.form');
/**
 * <p>SuperBoxSelect is an extension of the ComboBox component that displays selected items as labelled boxes within the form field. As seen on facebook, hotmail and other sites.</p>
 * <p>The SuperBoxSelect component was inspired by the BoxSelect component found here: http://efattal.fr/en/extjs/extuxboxselect/</p>
 *
 * @author <a href="mailto:dan.humphrey@technomedia.co.uk">Dan Humphrey</a>
 * @class Ext.ux.form.SuperBoxSelect
 * @extends Ext.form.ComboBox
 * @constructor
 * @component
 * @version 1.0b
 * @license TBA
 *
 */
Ext.ux.form.SuperBoxSelect = function(config) {
    Ext.ux.form.SuperBoxSelect.superclass.constructor.call(this,config);
    this.addEvents(
        /**
         * Fires before an item is added to the component via user interaction. Return false from the callback function to prevent the item from being added.
         * @event beforeadditem
         * @memberOf Ext.ux.form.SuperBoxSelect
         * @param {SuperBoxSelect} this
         * @param {Mixed} value The value of the item to be added
         */
        'beforeadditem',

        /**
         * Fires after a new item is added to the component.
         * @event additem
         * @memberOf Ext.ux.form.SuperBoxSelect
         * @param {SuperBoxSelect} this
         * @param {Mixed} value The value of the item which was added
         * @param {Record} record The store record which was added
         */
        'additem',

        /**
         * Fires when the allowAddNewData config is set to true, and a user attempts to add an item that is not in the data store.
         * @event newitem
         * @memberOf Ext.ux.form.SuperBoxSelect
         * @param {SuperBoxSelect} this
         * @param {Mixed} value The new item's value
         */
        'newitem',

        /**
         * Fires when an item's remove button is clicked. Return false from the callback function to prevent the item from being removed.
         * @event beforeremoveitem
         * @memberOf Ext.ux.form.SuperBoxSelect
         * @param {SuperBoxSelect} this
         * @param {Mixed} value The value of the item to be removed
         */
        'beforeremoveitem',

        /**
         * Fires after an item has been removed.
         * @event removeitem
         * @memberOf Ext.ux.form.SuperBoxSelect
         * @param {SuperBoxSelect} this
         * @param {Mixed} value The value of the item which was removed
         * @param {Record} record The store record which was removed
         */
        'removeitem',
        /**
         * Fires after the component values have been cleared.
         * @event clear
         * @memberOf Ext.ux.form.SuperBoxSelect
         * @param {SuperBoxSelect} this
         */
        'clear'
    );

};
/**
 * @private hide from doc gen
 */
Ext.ux.form.SuperBoxSelect = Ext.extend(Ext.ux.form.SuperBoxSelect,Ext.form.ComboBox,{
    /**
     * @cfg {Boolean} allowAddNewData When set to true, allows items to be added (via the setValueEx and addItem methods) that do not already exist in the data store. Defaults to false.
     */
    allowAddNewData: false,

    /**
     * @cfg {Boolean} backspaceDeletesLastItem When set to false, the BACKSPACE key will focus the last selected item. When set to true, the last item will be immediately deleted. Defaults to true.
     */
    backspaceDeletesLastItem: true,

    /**
     * @cfg {String} classField The underlying data field that will be used to supply an additional class to each item.
     */
    classField: null,

    /**
     * @cfg {String} clearBtnCls An additional class to add to the in-field clear button.
     */
    clearBtnCls: '',

    /**
     * @cfg {String/XTemplate} displayFieldTpl A template for rendering the displayField in each selected item. Defaults to null.
     */
    displayFieldTpl: null,

    /**
     * @cfg {String} extraItemCls An additional css class to apply to each item.
     */
    extraItemCls: '',

    /**
     * @cfg {String/Object/Function} extraItemStyle Additional css style(s) to apply to each item. Should be a valid argument to Ext.Element.applyStyles.
     */
    extraItemStyle: '',

    /**
     * @cfg {String} expandBtnCls An additional class to add to the in-field expand button.
     */
    expandBtnCls: '',

    /**
     * @cfg {Boolean} fixFocusOnTabSelect When set to true, the component will not lose focus when a list item is selected with the TAB key. Defaults to true.
     */
    fixFocusOnTabSelect: true,

     /**
     * @cfg {Boolean} forceFormValue When set to true, the component will always return a value to the parent form getValues method, and when the parent form is submitted manually. Defaults to false, meaning the component will only be included in the parent form submission (or getValues) if at least 1 item has been selected.
     */
    forceFormValue: true,

    /**
     * @cfg {Boolean} navigateItemsWithTab When set to true the tab key will navigate between selected items. Defaults to true.
     */
    navigateItemsWithTab: true,

    /**
     * @cfg {Boolean} pinList When set to true the select list will be pinned to allow for multiple selections. Defaults to true.
     */
    pinList: true,

    /**
     * @cfg {Boolean} preventDuplicates When set to true unique item values will be enforced. Defaults to true.
     */
    preventDuplicates: true,

    /**
     * @cfg {String} queryValuesDelimiter Used to delimit multiple values queried from the server when mode is remote.
     */
    queryValuesDelimiter: '|',

    /**
     * @cfg {String} queryValuesIndicator A request variable that is sent to the server (as true) to indicate that we are querying values rather than display data (as used in autocomplete) when mode is remote.
     */
    queryValuesInidicator: 'valuesqry',

    /**
     * @cfg {Boolean} removeValuesFromStore When set to true, selected records will be removed from the store. Defaults to true.
     */
    removeValuesFromStore: true,

    /**
     * @cfg {String} renderFieldBtns When set to true, will render in-field buttons for clearing the component, and displaying the list for selection. Defaults to true.
     */
    renderFieldBtns: true,

    /**
     * @cfg {Boolean} stackItems When set to true, the items will be stacked 1 per line. Defaults to false which displays the items inline.
     */
    stackItems: false,

    /**
     * @cfg {String} styleField The underlying data field that will be used to supply additional css styles to each item.
     */
    styleField : null,

     /**
     * @cfg {Boolean} supressClearValueRemoveEvents When true, the removeitem event will not be fired for each item when the clearValue method is called, or when the clear button is used. Defaults to false.
     */
    supressClearValueRemoveEvents : false,

    /**
     * @cfg {String/Boolean} validationEvent The event that should initiate field validation. Set to false to disable automatic validation (defaults to 'blur').
     */
	validationEvent : 'blur',

    /**
     * @cfg {String} valueDelimiter The delimiter to use when joining and splitting value arrays and strings.
     */
    valueDelimiter: ',',
    initComponent:function() {
       Ext.apply(this, {
            items           : new Ext.util.MixedCollection(false),
            usedRecords     : new Ext.util.MixedCollection(false),
            addedRecords	: [],
            remoteLookup	: [],
            hideTrigger     : true,
            grow            : false,
            resizable       : false,
            multiSelectMode : false,
            preRenderValue  : null
        });

        if(this.transform){
            this.doTransform();
        }

        if(this.forceFormValue){
        	this.items.on({
        	   add: this.manageNameAttribute,
        	   remove: this.manageNameAttribute,
        	   clear: this.manageNameAttribute,
        	   scope: this
        	})
        }

        Ext.ux.form.SuperBoxSelect.superclass.initComponent.call(this);
        if(this.mode === 'remote' && this.store){
        	this.store.on('load', this.onStoreLoad, this);
        }
    },
    onRender:function(ct, position) {
        Ext.ux.form.SuperBoxSelect.superclass.onRender.call(this, ct, position);

        this.el.dom.removeAttribute('name');


        var extraClass = (this.stackItems === true) ? 'x-superboxselect-stacked' : '';
        if(this.renderFieldBtns){
            extraClass += ' x-superboxselect-display-btns';
        }
        this.el.removeClass('x-form-text').addClass('x-superboxselect-input-field');

        this.wrapEl = this.el.wrap({
            tag : 'ul'
        });

        this.outerWrapEl = this.wrapEl.wrap({
            tag : 'div',
            cls: 'x-form-text x-superboxselect ' + extraClass
        });

        this.inputEl = this.el.wrap({
            tag : 'li',
            cls : 'x-superboxselect-input'
        });

        if(this.renderFieldBtns){
            this.setupFieldButtons().manageClearBtn();
        }

        this.setupFormInterception();

        if(this.preRenderValue){
            this.setValue(this.preRenderValue);
            this.preRenderValue = null;
        }
    },
    onStoreLoad : function(store, records, options){
    	//accomodating for bug in Ext 3.0.0 where options.params are empty
    	var q = options.params[this.queryParam] || store.baseParams[this.queryParam] || "",
    		isValuesQuery = options.params[this.queryValuesInidicator] || store.baseParams[this.queryValuesInidicator];

    	if(this.removeValuesFromStore){
    		this.store.each(function(record) {
				if(this.usedRecords.containsKey(record.get(this.valueField))){
					this.store.remove(record);
				}
			}, this);
    	}
    	//queried values
    	if(isValuesQuery){
    		var params = q.split(this.queryValuesDelimiter);
    		Ext.each(params,function(p){
    			this.remoteLookup.remove(p);
    			var rec = this.findRecord(this.valueField,p);
    			if(rec){
    				this.addRecord(rec);
    			}
    		},this);

    		if(this.setOriginal){
    			this.setOriginal = false;
    			this.originalValue = this.getValue();
    		}
    	}

    	//queried display (autocomplete) & addItem
    	if(q !== '' && this.allowAddNewData){
    		Ext.each(this.remoteLookup,function(r){
    			if(typeof r == "object" && r[this.displayField] == q){
    				this.remoteLookup.remove(r);
					if(records.length && records[0].get(this.displayField) === q) {
						this.addRecord(records[0]);
						return;
					}
					var rec = this.createRecord(r);
					this.store.add(rec);
		        	this.addRecord(rec);
		        	this.addedRecords.push(rec); //keep track of records added to store
		        	(function(){
		        		if(this.isExpanded()){
			        		this.collapse();
		        		}
		        	}).defer(10,this);
		        	return;
    			}
    		},this);
    	}

    	var toAdd = [];
    	if(q === ''){
	    	Ext.each(this.addedRecords,function(rec){
	    		if(this.preventDuplicates && this.usedRecords.containsKey(rec.get(this.valueField))){
					return;
	    		}
	    		toAdd.push(rec);

	    	},this);

    	}else{
    		var re = new RegExp(Ext.escapeRe(q) + '.*','i');
    		Ext.each(this.addedRecords,function(rec){
	    		if(this.preventDuplicates && this.usedRecords.containsKey(rec.get(this.valueField))){
					return;
	    		}
	    		if(re.test(rec.get(this.displayField))){
	    			toAdd.push(rec);
	    		}
	    	},this);
	    }
    	this.store.add(toAdd);
    	this.store.sort(this.displayField, 'ASC');

		if(this.store.getCount() === 0 && this.isExpanded()){
			this.collapse();
		}

	},
    doTransform : function() {
    	var s = Ext.getDom(this.transform), transformValues = [];
            if(!this.store){
                this.mode = 'local';
                var d = [], opts = s.options;
                for(var i = 0, len = opts.length;i < len; i++){
                    var o = opts[i],
                        value = (Ext.isIE && !Ext.isIE8 ? o.getAttributeNode('value').specified : o.hasAttribute('value')) ? o.value : o.text,
                        cls = (Ext.isIE && !Ext.isIE8 ? o.getAttributeNode('class').specified : o.hasAttribute('class')) ? o.className : '',
                        style = (Ext.isIE && !Ext.isIE8 ? o.getAttributeNode('style').specified : o.hasAttribute('style')) ? o.style : '';
                    if(o.selected) {
                        transformValues.push(value);
                    }
                    d.push([value, o.text, cls, style.cssText]);
                }
                this.store = new Ext.data.SimpleStore({
                    'id': 0,
                    fields: ['value', 'text', 'cls', 'style'],
                    data : d
                });
                Ext.apply(this,{
                    valueField: 'value',
                    displayField: 'text',
                    classField: 'cls',
                    styleField: 'style'
                });
            }

            if(transformValues.length){
                this.value = transformValues.join(',');
            }
    },
    setupFieldButtons : function(){
        this.buttonWrap = this.outerWrapEl.createChild({
            cls: 'x-superboxselect-btns'
        });

        this.buttonClear = this.buttonWrap.createChild({
            tag:'div',
            cls: 'x-superboxselect-btn-clear ' + this.clearBtnCls
        });

        this.buttonExpand = this.buttonWrap.createChild({
            tag:'div',
            cls: 'x-superboxselect-btn-expand ' + this.expandBtnCls
        });

        this.initButtonEvents();

        return this;
    },
    initButtonEvents : function() {
        this.buttonClear.addClassOnOver('x-superboxselect-btn-over').on('click', function(e) {
            e.stopEvent();
            if (this.disabled) {
                return;
            }
            this.clearValue();
            this.el.focus();
        }, this);

        this.buttonExpand.addClassOnOver('x-superboxselect-btn-over').on('click', function(e) {
            e.stopEvent();
            if (this.disabled) {
                return;
            }
            if (this.isExpanded()) {
                this.multiSelectMode = false;
            } else if (this.pinList) {
                this.multiSelectMode = true;
            }
            this.onTriggerClick();
        }, this);
    },
    removeButtonEvents : function() {
        this.buttonClear.removeAllListeners();
        this.buttonExpand.removeAllListeners();
        return this;
    },
    clearCurrentFocus : function(){
        if(this.currentFocus){
            this.currentFocus.onLnkBlur();
            this.currentFocus = null;
        }
        return this;
    },
    initEvents : function() {
        var el = this.el;

        el.on({
            click   : this.onClick,
            focus   : this.clearCurrentFocus,
            blur    : this.onBlur,

            keydown : this.onKeyDownHandler,
            keyup   : this.onKeyUpBuffered,

            scope   : this
        });

        this.on({
            collapse: this.onCollapse,
            expand: this.clearCurrentFocus,
            scope: this
        });

        this.wrapEl.on('click', this.onWrapClick, this);
        this.outerWrapEl.on('click', this.onWrapClick, this);

        this.inputEl.focus = function() {
            el.focus();
        };

        Ext.ux.form.SuperBoxSelect.superclass.initEvents.call(this);

        Ext.apply(this.keyNav, {
            tab: function(e) {
                if (this.fixFocusOnTabSelect && this.isExpanded()) {
                    e.stopEvent();
                    el.blur();
                    this.onViewClick(false);
                    this.focus(false, 10);
                    return true;
                }

                this.onViewClick(false);
                if (el.dom.value !== '') {
                    this.setRawValue('');
                }

                return true;
            },

            down: function(e) {
                if (!this.isExpanded() && !this.currentFocus) {
                    this.onTriggerClick();
                } else {
                    this.inKeyMode = true;
                    this.selectNext();
                }
            },

            enter: function(){}
        });
    },

    onClick: function() {
        this.clearCurrentFocus();
        this.collapse();
        this.autoSize();
    },

    beforeBlur: Ext.form.ComboBox.superclass.beforeBlur,

    onFocus: function() {
        this.outerWrapEl.addClass(this.focusClass);

        Ext.ux.form.SuperBoxSelect.superclass.onFocus.call(this);
    },

    onBlur: function() {
        this.outerWrapEl.removeClass(this.focusClass);

        this.clearCurrentFocus();

        if (this.el.dom.value !== '') {
            this.applyEmptyText();
            this.autoSize();
        }

        Ext.ux.form.SuperBoxSelect.superclass.onBlur.call(this);
    },

    onCollapse: function() {
    	this.view.clearSelections();
        this.multiSelectMode = false;
    },

    onWrapClick: function(e) {
        e.stopEvent();
        this.collapse();
        this.el.focus();
        this.clearCurrentFocus();
    },
    markInvalid : function(msg) {
        var elp, t;

        if (!this.rendered || this.preventMark ) {
            return;
        }
        this.outerWrapEl.addClass(this.invalidClass);
        msg = msg || this.invalidText;

        switch (this.msgTarget) {
            case 'qtip':
                Ext.apply(this.el.dom, {
                    qtip    : msg,
                    qclass  : 'x-form-invalid-tip'
                });
                Ext.apply(this.wrapEl.dom, {
                    qtip    : msg,
                    qclass  : 'x-form-invalid-tip'
                });
                if (Ext.QuickTips) { // fix for floating editors interacting with DND
                    Ext.QuickTips.enable();
                }
                break;
            case 'title':
                this.el.dom.title = msg;
                this.wrapEl.dom.title = msg;
                this.outerWrapEl.dom.title = msg;
                break;
            case 'under':
                if (!this.errorEl) {
                    elp = this.getErrorCt();
                    if (!elp) { // field has no container el
                        this.el.dom.title = msg;
                        break;
                    }
                    this.errorEl = elp.createChild({cls:'x-form-invalid-msg'});
                    this.errorEl.setWidth(elp.getWidth(true) - 20);
                }
                this.errorEl.update(msg);
                Ext.form.Field.msgFx[this.msgFx].show(this.errorEl, this);
                break;
            case 'side':
                if (!this.errorIcon) {
                    elp = this.getErrorCt();
                    if (!elp) { // field has no container el
                        this.el.dom.title = msg;
                        break;
                    }
                    this.errorIcon = elp.createChild({cls:'x-form-invalid-icon'});
                }
                this.alignErrorIcon();
                Ext.apply(this.errorIcon.dom, {
                    qtip    : msg,
                    qclass  : 'x-form-invalid-tip'
                });
                this.errorIcon.show();
                this.on('resize', this.alignErrorIcon, this);
                break;
            default:
                t = Ext.getDom(this.msgTarget);
                t.innerHTML = msg;
                t.style.display = this.msgDisplay;
                break;
        }
        this.fireEvent('invalid', this, msg);
    },
    clearInvalid : function(){
        if(!this.rendered || this.preventMark){ // not rendered
            return;
        }
        this.outerWrapEl.removeClass(this.invalidClass);
        switch(this.msgTarget){
            case 'qtip':
                this.el.dom.qtip = '';
                this.wrapEl.dom.qtip ='';
                break;
            case 'title':
                this.el.dom.title = '';
                this.wrapEl.dom.title = '';
                this.outerWrapEl.dom.title = '';
                break;
            case 'under':
                if(this.errorEl){
                    Ext.form.Field.msgFx[this.msgFx].hide(this.errorEl, this);
                }
                break;
            case 'side':
                if(this.errorIcon){
                    this.errorIcon.dom.qtip = '';
                    this.errorIcon.hide();
                    this.un('resize', this.alignErrorIcon, this);
                }
                break;
            default:
                var t = Ext.getDom(this.msgTarget);
                t.innerHTML = '';
                t.style.display = 'none';
                break;
        }
        this.fireEvent('valid', this);
    },
    alignErrorIcon : function(){
        if(this.wrap){
            this.errorIcon.alignTo(this.wrap, 'tl-tr', [Ext.isIE ? 5 : 2, 3]);
        }
    },
    expand : function(){
        if (this.isExpanded() || !this.hasFocus) {
            return;
        }
        this.list.alignTo(this.outerWrapEl, this.listAlign).show();
        this.innerList.setOverflow('auto'); // necessary for FF 2.0/Mac
        Ext.getDoc().on({
            mousewheel: this.collapseIf,
            mousedown: this.collapseIf,
            scope: this
        });
        this.fireEvent('expand', this);
    },
    restrictHeight : function(){
        var inner = this.innerList.dom,
            st = inner.scrollTop,
            list = this.list;

        inner.style.height = '';

        var pad = list.getFrameWidth('tb')+(this.resizable?this.handleHeight:0)+this.assetHeight,
            h = Math.max(inner.clientHeight, inner.offsetHeight, inner.scrollHeight),
            ha = this.getPosition()[1]-Ext.getBody().getScroll().top,
            hb = Ext.lib.Dom.getViewHeight()-ha-this.getSize().height,
            space = Math.max(ha, hb, this.minHeight || 0)-list.shadowOffset-pad-5;

        h = Math.min(h, space, this.maxHeight);
        this.innerList.setHeight(h);

        list.beginUpdate();
        list.setHeight(h+pad);
        list.alignTo(this.outerWrapEl, this.listAlign);
        list.endUpdate();

        if(this.multiSelectMode){
            inner.scrollTop = st;
        }
    },

    validateValue: function(val){
        if(this.items.getCount() === 0){
             if(this.allowBlank){
                 this.clearInvalid();
                 return true;
             }else{
                 this.markInvalid(this.blankText);
                 return false;
             }
        }

        this.clearInvalid();
        return true;
    },

    manageNameAttribute :  function(){
    	if(this.items.getCount() === 0){
    	   this.el.dom.setAttribute('name', this.name);
    	}else{
    		this.el.dom.removeAttribute('name');
    	}
    },
    setupFormInterception : function(){
        var form;
        this.findParentBy(function(p){
            if(p.getForm){
                form = p.getForm();
            }
        });
        if(form){

        	var formGet = form.getValues;
            form.getValues = function(asString){
                this.el.dom.disabled = true;
                var oldVal = this.el.dom.value;
                this.setRawValue('');
                var vals = formGet.call(form);
                this.el.dom.disabled = false;
                this.setRawValue(oldVal);
                if(this.forceFormValue && this.items.getCount() === 0){
                	vals[this.name] = '';
                }
                return asString ? Ext.urlEncode(vals) : vals ;
            }.createDelegate(this);
        }
    },
    onResize : function(w, h, rw, rh) {
        var reduce = Ext.isIE6 ? 4 : Ext.isIE7 ? 1 : Ext.isIE8 ? 1 : 0;

        this._width = w;
        this.outerWrapEl.setWidth(w - reduce);
        if (this.renderFieldBtns) {
            reduce += (this.buttonWrap.getWidth() + 20);
            this.wrapEl.setWidth(w - reduce);
        }
        Ext.ux.form.SuperBoxSelect.superclass.onResize.call(this, w, h, rw, rh);
        this.autoSize();
    },
    onEnable: function(){
        Ext.ux.form.SuperBoxSelect.superclass.onEnable.call(this);
        this.items.each(function(item){
            item.enable();
        });
        if (this.renderFieldBtns) {
            this.initButtonEvents();
        }
    },
    onDisable: function(){
        Ext.ux.form.SuperBoxSelect.superclass.onDisable.call(this);
        this.items.each(function(item){
            item.disable();
        });
        if(this.renderFieldBtns){
            this.removeButtonEvents();
        }
    },
    /**
     * Clears all values from the component.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name clearValue
     * @param {Boolean} supressRemoveEvent [Optional] When true, the 'removeitem' event will not fire for each item that is removed.
     */
    clearValue : function(supressRemoveEvent){
        Ext.ux.form.SuperBoxSelect.superclass.clearValue.call(this);
        this.preventMultipleRemoveEvents = supressRemoveEvent || this.supressClearValueRemoveEvents || false;
    	this.removeAllItems();
        this.fireEvent('clear',this);
        return this;
    },
    onKeyUp : function(e) {
        if (this.editable !== false && !e.isSpecialKey() && (!e.hasModifier() || e.shiftKey)) {
            this.lastKey = e.getKey();
            this.dqTask.delay(this.queryDelay);
        }
    },
    onKeyDownHandler : function(e,t) {

        var toDestroy,nextFocus,idx;
        if ((e.getKey() === e.DELETE || e.getKey() === e.SPACE) && this.currentFocus){
            e.stopEvent();
            toDestroy = this.currentFocus;
            this.on('expand',function(){this.collapse();},this,{single: true});
            idx = this.items.indexOfKey(this.currentFocus.key);

            this.clearCurrentFocus();

            if(idx < (this.items.getCount() -1)){
                nextFocus = this.items.itemAt(idx+1);
            }

            toDestroy.preDestroy(true);
            if(nextFocus){
                (function(){
                    nextFocus.onLnkFocus();
                    this.currentFocus = nextFocus;
                }).defer(200,this);
            }

            return true;
        }

        var val = this.el.dom.value, it, ctrl = e.ctrlKey;
        if(e.getKey() === e.ENTER){
            e.stopEvent();
            if (val !== "") {
                if (ctrl || !this.isExpanded())  {  //ctrl+enter for new items
                	this.view.clearSelections();
                    this.collapse();
                    this.setRawValue('');
                    this.fireEvent('newitem', this, val);
                }
                else {
                	this.onViewClick();
                    //removed from 3.0.1
                    if(this.unsetDelayCheck){
                        this.delayedCheck = true;
                        this.unsetDelayCheck.defer(10, this);
                    }
                }
            }else{
                if(!this.isExpanded()){
                    return;
                }
                this.onViewClick();
                //removed from 3.0.1
                if(this.unsetDelayCheck){
                    this.delayedCheck = true;
                    this.unsetDelayCheck.defer(10, this);
                }
            }
            return true;
        }

        if(val !== '') {
            this.autoSize();
            return;
        }

        //select first item
        if(e.getKey() === e.HOME){
            e.stopEvent();
            if(this.items.getCount() > 0){
                this.collapse();
                it = this.items.get(0);
                it.el.focus();

            }
            return true;
        }
        //backspace remove
        if(e.getKey() === e.BACKSPACE){
            e.stopEvent();
            if(this.currentFocus) {
                toDestroy = this.currentFocus;
                this.on('expand',function(){
                    this.collapse();
                },this,{single: true});

                idx = this.items.indexOfKey(toDestroy.key);

                this.clearCurrentFocus();
                if(idx < (this.items.getCount() -1)){
                    nextFocus = this.items.itemAt(idx+1);
                }

                toDestroy.preDestroy(true);

                if(nextFocus){
                    (function(){
                        nextFocus.onLnkFocus();
                        this.currentFocus = nextFocus;
                    }).defer(200,this);
                }

                return;
            }else{
                it = this.items.get(this.items.getCount() -1);
                if(it){
                    if(this.backspaceDeletesLastItem){
                        this.on('expand',function(){this.collapse();},this,{single: true});
                        it.preDestroy(true);
                    }else{
                        if(this.navigateItemsWithTab){
                            it.onElClick();
                        }else{
                            this.on('expand',function(){
                                this.collapse();
                                this.currentFocus = it;
                                this.currentFocus.onLnkFocus.defer(20,this.currentFocus);
                            },this,{single: true});
                        }
                    }
                }
                return true;
            }
        }

        if(!e.isNavKeyPress()){
            this.multiSelectMode = false;
            this.clearCurrentFocus();
            return;
        }
        //arrow nav
        if(e.getKey() === e.LEFT || (e.getKey() === e.UP && !this.isExpanded())){
            e.stopEvent();
            this.collapse();
            //get last item
            it = this.items.get(this.items.getCount()-1);
            if(this.navigateItemsWithTab){
                //focus last el
                if(it){
                    it.focus();
                }
            }else{
                //focus prev item
                if(this.currentFocus){
                    idx = this.items.indexOfKey(this.currentFocus.key);
                    this.clearCurrentFocus();

                    if(idx !== 0){
                        this.currentFocus = this.items.itemAt(idx-1);
                        this.currentFocus.onLnkFocus();
                    }
                }else{
                    this.currentFocus = it;
                    if(it){
                        it.onLnkFocus();
                    }
                }
            }
            return true;
        }
        if(e.getKey() === e.DOWN){
            if(this.currentFocus){
                this.collapse();
                e.stopEvent();
                idx = this.items.indexOfKey(this.currentFocus.key);
                if(idx == (this.items.getCount() -1)){
                    this.clearCurrentFocus.defer(10,this);
                }else{
                    this.clearCurrentFocus();
                    this.currentFocus = this.items.itemAt(idx+1);
                    if(this.currentFocus){
                        this.currentFocus.onLnkFocus();
                    }
                }
                return true;
            }
        }
        if(e.getKey() === e.RIGHT){
            this.collapse();
            it = this.items.itemAt(0);
            if(this.navigateItemsWithTab){
                //focus first el
                if(it){
                    it.focus();
                }
            }else{
                if(this.currentFocus){
                    idx = this.items.indexOfKey(this.currentFocus.key);
                    this.clearCurrentFocus();
                    if(idx < (this.items.getCount() -1)){
                        this.currentFocus = this.items.itemAt(idx+1);
                        if(this.currentFocus){
                            this.currentFocus.onLnkFocus();
                        }
                    }
                }else{
                    this.currentFocus = it;
                    if(it){
                        it.onLnkFocus();
                    }
                }
            }
        }
    },
    onKeyUpBuffered : function(e){
        if(!e.isNavKeyPress()){
            this.autoSize();
        }
    },
    reset :  function(){
        Ext.ux.form.SuperBoxSelect.superclass.reset.call(this);
        this.addedRecords = [];
        this.autoSize().setRawValue('');
    },
    applyEmptyText : function(){
        if(this.items.getCount() > 0){
            this.el.removeClass(this.emptyClass);
            this.setRawValue('');
            return this;
        }
        if(this.rendered && this.emptyText && this.getRawValue().length < 1){
            this.setRawValue(this.emptyText);
            this.el.addClass(this.emptyClass);
        }
        return this;
    },
    /**
     * @private
     *
     * Use clearValue instead
     */
    removeAllItems: function(){
    	this.items.each(function(item){
            item.preDestroy(true);
        },this);
        this.manageClearBtn();
        return this;
    },
    resetStore: function(){
        this.store.clearFilter();
        if(!this.removeValuesFromStore){
            return this;
        }
        this.usedRecords.each(function(rec){
            this.store.add(rec);
        },this);
        this.sortStore();
        return this;
    },
    sortStore: function(){
        var ss = this.store.getSortState();
        if(ss && ss.field){
            this.store.sort(ss.field, ss.direction);
        }
        return this;
    },
    getCaption: function(dataObject){
        if(typeof this.displayFieldTpl === 'string') {
            this.displayFieldTpl = new Ext.XTemplate(this.displayFieldTpl);
        }
        var caption, recordData = dataObject instanceof Ext.data.Record ? dataObject.data : dataObject;

        if(this.displayFieldTpl) {
            caption = this.displayFieldTpl.apply(recordData);
        } else if(this.displayField) {
            caption = recordData[this.displayField];
        }

        return caption;
    },
    addRecord : function(record) {
        var display = record.data[this.displayField],
            caption = this.getCaption(record),
            val = record.data[this.valueField],
            cls = this.classField ? record.data[this.classField] : '',
            style = this.styleField ? record.data[this.styleField] : '';

        if (this.removeValuesFromStore) {
            this.usedRecords.add(val, record);
            this.store.remove(record);
        }

        this.addItemBox(val, display, caption, cls, style);
        this.fireEvent('additem', this, val, record);
    },
    createRecord : function(recordData){
        if(!this.recordConstructor){
            var recordFields = [
                {name: this.valueField},
                {name: this.displayField}
            ];
            if(this.classField){
                recordFields.push({name: this.classField});
            }
            if(this.styleField){
                recordFields.push({name: this.styleField});
            }
            this.recordConstructor = Ext.data.Record.create(recordFields);
        }
        return new this.recordConstructor(recordData);
    },
    /**
     * Adds an array of items to the SuperBoxSelect component if the {@link #Ext.ux.form.SuperBoxSelect-allowAddNewData} config is set to true.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name addItem
     * @param {Array} newItemObjects An Array of object literals containing the property names and values for an item. The property names must match those specified in {@link #Ext.ux.form.SuperBoxSelect-displayField}, {@link #Ext.ux.form.SuperBoxSelect-valueField} and {@link #Ext.ux.form.SuperBoxSelect-classField}
     */
    addItems : function(newItemObjects){
    	if (Ext.isArray(newItemObjects)) {
			Ext.each(newItemObjects, function(item) {
				this.addItem(item);
			}, this);
		} else {
			this.addItem(newItemObjects);
		}
    },
    /**
     * Adds an item to the SuperBoxSelect component if the {@link #Ext.ux.form.SuperBoxSelect-allowAddNewData} config is set to true.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name addItem
     * @param {Object} newItemObject An object literal containing the property names and values for an item. The property names must match those specified in {@link #Ext.ux.form.SuperBoxSelect-displayField}, {@link #Ext.ux.form.SuperBoxSelect-valueField} and {@link #Ext.ux.form.SuperBoxSelect-classField}
     */
    addItem : function(newItemObject){

        var val = newItemObject[this.valueField];

        if(this.disabled) {
            return false;
        }
        if(this.preventDuplicates && this.hasValue(val)){
            return;
        }

        //use existing record if found
        var record = this.findRecord(this.valueField, val);
        if (record) {
            this.addRecord(record);
            return;
        } else if (!this.allowAddNewData) { // else it's a new item
            return;
        }

        if(this.mode === 'remote'){
        	this.remoteLookup.push(newItemObject);
        	this.doQuery(val,false,false);
        	return;
        }

        var rec = this.createRecord(newItemObject);
        this.store.add(rec);
        this.addRecord(rec);

        return true;
    },
    addItemBox : function(itemVal,itemDisplay,itemCaption, itemClass, itemStyle) {
        var parseStyle = function(s){
            var ret = '';
            if(typeof s == 'function'){
                ret = s.call();
            }else if(typeof s == 'object'){
                for(var p in s){
                    ret+= p +':'+s[p]+';';
                }
            }else if(typeof s == 'string'){
                ret = s + ';';
            }
            return ret;
        };
        var itemKey = Ext.id(null,'sbx-item');
        var box = new Ext.ux.form.SuperBoxSelectItem({
            owner: this,
            disabled: this.disabled,
            renderTo: this.wrapEl,
            cls: this.extraItemCls + ' ' + itemClass,
            style: parseStyle(this.extraItemStyle) + ' ' + itemStyle,
            caption: itemCaption,
            display: itemDisplay,
            value:  itemVal,
            key: itemKey,
            listeners: {
                'remove': function(item){
                    if(this.fireEvent('beforeremoveitem',this,item.value) === false){
                        return;
                    }
                    this.items.removeKey(item.key);
                    if(this.removeValuesFromStore){
                        if(this.usedRecords.containsKey(item.value)){
                            this.store.add(this.usedRecords.get(item.value));
                            this.usedRecords.removeKey(item.value);
                            this.sortStore();
                            if(this.view){
                                this.view.render();
                            }
                        }
                    }
                    if(!this.preventMultipleRemoveEvents){
                    	this.fireEvent.defer(250,this,['removeitem',this,item.value, this.findInStore(item.value)]);
                    }
                    this.preventMultipleRemoveEvents = false;
                },
                destroy: function(){
                    this.collapse();
                    this.autoSize().manageClearBtn().validateValue();
                },
                scope: this
            }
        });
        box.render();

        if(itemVal != ''){
            box.hidden = this.el.insertSibling({
                tag:'input',
                type:'hidden',
                value: itemVal,
                name: (this.hiddenName || this.name)
            },'before');
        }

        this.items.add(itemKey,box);
        this.applyEmptyText().autoSize().manageClearBtn().validateValue();
    },
    manageClearBtn : function() {
        if (!this.renderFieldBtns || !this.rendered) {
            return this;
        }
        var cls = 'x-superboxselect-btn-hide';
        if (this.items.getCount() === 0) {
            this.buttonClear.addClass(cls);
        } else {
            this.buttonClear.removeClass(cls);
        }
        return this;
    },
    findInStore : function(val){
        var index = this.store.find(this.valueField, val);
        if(index > -1){
            return this.store.getAt(index);
        }
        return false;
    },
    /**
     * Returns a String value containing a concatenated list of item values. The list is concatenated with the {@link #Ext.ux.form.SuperBoxSelect-valueDelimiter}.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name getValue
     * @return {String} a String value containing a concatenated list of item values.
     */
    getValue : function() {
        var ret = [];
        this.items.each(function(item){
            if(item != '')
                ret.push(item.value);
        });
        return ret.join(this.valueDelimiter);
    },
    /**
     * Returns an Array of item objects containing the {@link #Ext.ux.form.SuperBoxSelect-displayField}, {@link #Ext.ux.form.SuperBoxSelect-valueField} and {@link #Ext.ux.form.SuperBoxSelect-classField} properties.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name getValueEx
     * @return {Array} an array of item objects.
     */
    getValueEx : function() {
        var ret = [];
        this.items.each(function(item){
            if(item.value != ''){
                var newItem = {};
                newItem[this.valueField] = item.value;
                newItem[this.displayField] = item.display;
                newItem[this.classField]= item.cls;
                ret.push(newItem);
            }
        },this);
        return ret;
    },
    // private
    initValue : function(){

        Ext.ux.form.SuperBoxSelect.superclass.initValue.call(this);
        if(this.mode === 'remote') {
        	this.setOriginal = true;
        }
    },
    /**
     * Sets the value of the SuperBoxSelect component.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name setValue
     * @param {String|Array} value An array of item values, or a String value containing a delimited list of item values. (The list should be delimited with the {@link #Ext.ux.form.SuperBoxSelect-valueDelimiter)
     */
    setValue : function(value){
        if(!this.rendered){
            this.preRenderValue = value;
            return;
        }

        var values = Ext.isArray(value) ? value : value.split(this.valueDelimiter);
        this.removeAllItems().resetStore();

        //reset remoteLookup because setValue should overwrite everything
        //inc pending data
        this.remoteLookup = [];

        Ext.each(values,function(val){
            var record = this.findRecord(this.valueField, val);
            if(record){
                this.addRecord(record);
            }else if(this.mode === 'remote'){
				this.remoteLookup.push(val);
            }
        },this);

        if(this.mode === 'remote'){
      		var q = this.remoteLookup.join(this.queryValuesDelimiter);
      		this.doQuery(q,false, true); //3rd param to specify a values query
        }

    },
    /**
     * Sets the value of the SuperBoxSelect component, adding new items that don't exist in the data store if the {@link #Ext.ux.form.SuperBoxSelect-allowAddNewData} config is set to true.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name setValue
     * @param {Array} data An Array of item objects containing the {@link #Ext.ux.form.SuperBoxSelect-displayField}, {@link #Ext.ux.form.SuperBoxSelect-valueField} and {@link #Ext.ux.form.SuperBoxSelect-classField} properties.
     */
    setValueEx : function(data){
        this.removeAllItems().resetStore();

        if(!Ext.isArray(data)){
            data = [data];
        }
        this.remoteLookup = [];

        if(this.allowAddNewData && this.mode === 'remote'){ // no need to query
            Ext.each(data, function(d){
            	var r = this.findRecord(this.valueField, d[this.valueField]) || this.createRecord(d);
                this.addRecord(r);
            },this);
            return;
        }

        Ext.each(data,function(item){
            this.addItem(item);
        },this);
    },
    /**
     * Returns true if the SuperBoxSelect component has a selected item with a value matching the 'val' parameter.
     * @methodOf Ext.ux.form.SuperBoxSelect
     * @name hasValue
     * @param {Mixed} val The value to test.
     * @return {Boolean} true if the component has the selected value, false otherwise.
     */
    hasValue: function(val){
        var has = false;
        this.items.each(function(item){
            if(item.value == val){
                has = true;
                return false;
            }
        },this);
        return has;
    },
    onSelect : function(record, index) {
        var val = record.data[this.valueField];

        if(this.preventDuplicates && this.hasValue(val)){
            return;
        }

        this.setRawValue('');
        this.lastSelectionText = '';

        if(this.fireEvent('beforeadditem',this,val) !== false){
            this.addRecord(record);
        }
        if(this.store.getCount() === 0 || !this.multiSelectMode){
            this.collapse();
        }else{
            this.restrictHeight();
        }
    },
    onDestroy : function() {
        this.items.each(function(item) {
            item.preDestroy(true);
        }, this);

        if (this.renderFieldBtns) {
            Ext.destroy(
                this.buttonClear,
                this.buttonExpand,
                this.buttonWrap
            );
        }

        Ext.destroy(
            this.inputEl,
            this.wrapEl,
            this.outerWrapEl
        );

        Ext.ux.form.SuperBoxSelect.superclass.onDestroy.call(this);
    },
    autoSize : function(){
        if(!this.rendered){
            return this;
        }
        if(!this.metrics){
            this.metrics = Ext.util.TextMetrics.createInstance(this.el);
        }
        var el = this.el,
            v = el.dom.value,
            d = document.createElement('div');

        if(v === "" && this.emptyText && this.items.getCount() < 1){
            v = this.emptyText;
        }
        d.appendChild(document.createTextNode(v));
        v = d.innerHTML;
        d = null;
        v += "&#160;";
        var w = Math.max(this.metrics.getWidth(v) +  24, 24);
        if(typeof this._width != 'undefined'){
            w = Math.min(this._width, w);
        }
        this.el.setWidth(w);

        if(Ext.isIE){
            this.el.dom.style.top='0';
        }
        return this;
    },
    doQuery : function(q, forceAll,valuesQuery){
        q = Ext.isEmpty(q) ? '' : q;
        var qe = {
            query: q,
            forceAll: forceAll,
            combo: this,
            cancel:false
        };
        if(this.fireEvent('beforequery', qe)===false || qe.cancel){
            return false;
        }
        q = qe.query;
        forceAll = qe.forceAll;
        if(forceAll === true || (q.length >= this.minChars)){
            if(this.lastQuery !== q){
            	this.lastQuery = q;
                if(this.mode == 'local'){
                    this.selectedIndex = -1;
                    if(forceAll){
                        this.store.clearFilter();
                    }else{
                        this.store.filter(this.displayField, q);
                    }
                    this.onLoad();
                }else{

                    this.store.baseParams[this.queryParam] = q;
                    this.store.baseParams[this.queryValuesInidicator] = valuesQuery;
                    this.store.load({
                        params: this.getParams(q)
                    });
                    this.expand();
                }
            }else{
                this.selectedIndex = -1;
                this.onLoad();
            }
        }
    }
});
Ext.reg('superboxselect', Ext.ux.form.SuperBoxSelect);
/*
 * @private
 */
Ext.ux.form.SuperBoxSelectItem = function(config){
    Ext.apply(this,config);
    Ext.ux.form.SuperBoxSelectItem.superclass.constructor.call(this);
};
/*
 * @private
 */
Ext.ux.form.SuperBoxSelectItem = Ext.extend(Ext.ux.form.SuperBoxSelectItem,Ext.Component, {
    initComponent : function(){
        Ext.ux.form.SuperBoxSelectItem.superclass.initComponent.call(this);
    },
    onElClick : function(e){
        var o = this.owner;
        o.clearCurrentFocus().collapse();
        if(o.navigateItemsWithTab){
            this.focus();
        }else{
            o.el.dom.focus();
            var that = this;
            (function(){
                this.onLnkFocus();
                o.currentFocus = this;
            }).defer(10,this);
        }
    },

    onLnkClick : function(e){
        if(e) {
            e.stopEvent();
        }
        this.preDestroy();
        if(!this.owner.navigateItemsWithTab){
            this.owner.el.focus();
        }
    },
    onLnkFocus : function(){
        this.el.addClass("x-superboxselect-item-focus");
        this.owner.outerWrapEl.addClass("x-form-focus");
    },

    onLnkBlur : function(){
        this.el.removeClass("x-superboxselect-item-focus");
        this.owner.outerWrapEl.removeClass("x-form-focus");
    },

    enableElListeners : function() {
        this.el.on('click', this.onElClick, this, {stopEvent:true});

        this.el.addClassOnOver('x-superboxselect-item x-superboxselect-item-hover');
    },

    enableLnkListeners : function() {
        this.lnk.on({
            click: this.onLnkClick,
            focus: this.onLnkFocus,
            blur:  this.onLnkBlur,
            scope: this
        });
    },

    enableAllListeners : function() {
        this.enableElListeners();
        this.enableLnkListeners();
    },
    disableAllListeners : function() {
        this.el.removeAllListeners();
        this.lnk.un('click', this.onLnkClick, this);
        this.lnk.un('focus', this.onLnkFocus, this);
        this.lnk.un('blur', this.onLnkBlur, this);
    },
    onRender : function(ct, position){

        Ext.ux.form.SuperBoxSelectItem.superclass.onRender.call(this, ct, position);

        var el = this.el;
        if(el){
            el.remove();
        }

        this.el = el = ct.createChild({ tag: 'li' }, ct.last());
        el.addClass('x-superboxselect-item');

        var btnEl = this.owner.navigateItemsWithTab ? ( Ext.isSafari ? 'button' : 'a') : 'span';
        var itemKey = this.key;

        Ext.apply(el, {
            focus: function(){
                var c = this.down(btnEl +'.x-superboxselect-item-close');
                if(c){
                	c.focus();
                }
            },
            preDestroy: function(){
                this.preDestroy();
            }.createDelegate(this)
        });

        this.enableElListeners();

        el.update(this.caption);

        var cfg = {
            tag: btnEl,
            'class': 'x-superboxselect-item-close',
            tabIndex : this.owner.navigateItemsWithTab ? '0' : '-1'
        };
        if(btnEl === 'a'){
            cfg.href = '#';
        }
        this.lnk = el.createChild(cfg);


        if(!this.disabled) {
            this.enableLnkListeners();
        }else {
            this.disableAllListeners();
        }

        this.on({
            disable: this.disableAllListeners,
            enable: this.enableAllListeners,
            scope: this
        });

        this.setupKeyMap();
    },
    setupKeyMap : function(){
        new Ext.KeyMap(this.lnk, [
            {
                key: [
                    Ext.EventObject.BACKSPACE,
                    Ext.EventObject.DELETE,
                    Ext.EventObject.SPACE
                ],
                fn: this.preDestroy,
                scope: this
            }, {
                key: [
                    Ext.EventObject.RIGHT,
                    Ext.EventObject.DOWN
                ],
                fn: function(){
                    this.moveFocus('right');
                },
                scope: this
            },
            {
                key: [Ext.EventObject.LEFT,Ext.EventObject.UP],
                fn: function(){
                    this.moveFocus('left');
                },
                scope: this
            },
            {
                key: [Ext.EventObject.HOME],
                fn: function(){
                    var l = this.owner.items.get(0).el.focus();
                    if(l){
                        l.el.focus();
                    }
                },
                scope: this
            },
            {
                key: [Ext.EventObject.END],
                fn: function(){
                    this.owner.el.focus();
                },
                scope: this
            },
            {
                key: Ext.EventObject.ENTER,
                fn: function(){
                }
            }
        ]).stopEvent = true;
    },
    moveFocus : function(dir) {
        var el = this.el[dir == 'left' ? 'prev' : 'next']() || this.owner.el;

        el.focus.defer(100,el);
    },

    preDestroy : function(supressEffect) {
    	if(this.fireEvent('remove', this) === false){
	    	return;
	    }
    	var actionDestroy = function(){
            if(this.owner.navigateItemsWithTab){
                this.moveFocus('right');
            }
            this.hidden.remove();
            this.hidden = null;
            this.destroy();
        };

        if(supressEffect){
            actionDestroy.call(this);
        } else {
            this.el.hide({
                duration: 0.2,
                callback: actionDestroy,
                scope: this
            });
        }
        return this;
    },
    onDestroy : function() {
        Ext.destroy(
            this.lnk,
            this.el
        );

        Ext.ux.form.SuperBoxSelectItem.superclass.onDestroy.call(this);
    }
});