
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

