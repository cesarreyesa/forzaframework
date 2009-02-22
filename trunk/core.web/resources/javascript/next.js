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

Ext.Updater.defaults.loadScripts = true;

Ext.Ajax.on("requestexception", function(conn, response, options){
//    var dialog = new Ext.Window({title: 'Error', autoCreate:true, modal:false, width:600, height:390, shadow:true, minWidth:200, minHeight:200, proxyDrag: true });
    if(response.status == '403'){
        Ext.MessageBox.show({
            title:'Acceso denegado', msg: 'No tiene permiso para acceder al recurso solicitado.', buttons:Ext.MessageBox.OK, icon:Ext.MessageBox.ERROR
        });
    }else if(response.status == '0'){
        Ext.MessageBox.show({
            title:'Problema de red', msg: 'No se pudo establecer una conexion con el servidor, verifique su conexion de red.', buttons:Ext.MessageBox.OK
        });
    }else if(response.status == '404'){
        Ext.MessageBox.show({
            title:'Error', msg: 'El sistema se encuentra en mantenimiento, por favor intente mas tarde.', buttons:Ext.MessageBox.OK
        });
    }else{
        var text = response.responseText && response.responseText.length > 500 ? response.responseText.substr(0, 500) : response.responseText;
        Ext.Msg.alert("Error", "There has an error. Please try again.<br/>Status: " + response.status + "" +
                               "<br />Status Text: " + text + "<br/>");
    }
    if(options && options.scope && options.scope.el){
        options.scope.el.update('');
    }
});

Ext.form.DateField.format='d/m/Y';

// A reusable error reader class for XML forms
Ext.form.XmlErrorReader = function(){
    Ext.form.XmlErrorReader.superclass.constructor.call(this, {
            record : 'field',
            success: '@success'
        }, [
            'id', 'msg'
        ]
    );
};
Ext.extend(Ext.form.XmlErrorReader, Ext.data.XmlReader);

// Si algun error no tiene id, entonces lo presenta como un error general en un alert.
Ext.form.BasicForm.prototype.markInvalid = function(errors){
    if(Ext.isArray(errors)){
        var msg = '';
        for(var i = 0, len = errors.length; i < len; i++){
            var fieldError = errors[i];
            if(fieldError.id != ''){
                var f = this.findField(fieldError.id);
                if(f){
                    f.markInvalid(fieldError.msg);
                }
            }
            else{
                msg = fieldError.msg + '<br />';
            }
        }
        if(msg != ''){
            Ext.Msg.alert('Error', msg);
        }
    }else{
        var field, id;
        for(id in errors){
            if(typeof errors[id] != 'function' && (field = this.findField(id))){
                field.markInvalid(errors[id]);
            }
        }
    }
    return this;
}


Ext.form.BasicForm.prototype.destroy = function(){
    this.items.each(function(f){
        Ext.destroy(f);
    });
    if(this.el){
    	if(this.el.removeAllListeners){
			this.el.removeAllListeners();
    	}
    	if(this.el.remove){
			this.el.remove();
    	}
    }
	this.purgeListeners();
}

// Remueve el elemento seleccionado si existe o el indice que le pasen.
Ext.form.ComboBox.prototype.remove = function(index){
    index |= this.selectedIndex;
    this.store.remove(this.store.getAt(index));
    this.clearValue();
}

Ext.form.ComboBox.prototype.getText = function(){
    return this.el.dom.value;
}

// Acepta un url y crean un proxy nuevo/
Ext.data.Store.prototype.reload = function(options){
    if(options && options.url){
        this.proxy = new Ext.data.HttpProxy({ url: options.url});
    }
    this.load(Ext.applyIf(options||{}, this.lastOptions));
}

// Remueve todos los items del contenedor
Ext.Container.prototype.removeAll = function(){
    var items = this.items;
    if(items && items.length > 0){
        items.each(function(item){
            this.remove(item, true);
        });
    }
}

// Remueve los elementos del container y Crea un panel nuevo con fit layout y asigna ahi el panel
Ext.Panel.prototype.replacePanel = function(newPanel){
    this.layout.container.removeAll();

    var panel = new Ext.Panel({
        layout:'fit',
        border:false,
        items:newPanel
    });

    this.layout.container.add(panel);
    this.layout.setActiveItem(panel);

}

// Si el panel es de tipo CardLayout entonces remueve los elementos actuales.
Ext.Panel.prototype.load = function(){
    if(this.id == 'center' && Ext.get('dc')){
        Ext.get('dc').update('');
    }
    if(this.layout instanceof Ext.layout.CardLayout){
        this.layout.container.removeAll();
    }
    var um = this.body.getUpdater();
    um.update.apply(um, arguments);
    return this;
}

// Agregar un elemento buscando la definicion de record y creando valores por default.
Ext.data.Record.AUTO_ID = -1000;
Ext.data.Store.prototype.add = function(records){
    if(!records){
        var fields = this.reader.recordType.prototype.fields;
        records = new this.reader.recordType({}, ++Ext.data.Record.AUTO_ID);
        for(var i = 0; i<fields.items.length;i++){
            var field = fields.items[i];
            if(field.type == 'string'){
                records.data[field.name] = '';
            }else if(field.type == 'float'){
                records.data[field.name] = 0;
            }else if(field.type == 'date'){
                records.data[field.name] = new Date();
            }
        }
    }
    records = [].concat(records);
    for(var i = 0, len = records.length; i < len; i++){
        records[i].join(this);
    }
    var index = this.data.length;
    this.data.addAll(records);
    if(this.snapshot){
        this.snapshot.addAll(records);
    }
    this.fireEvent("add", this, records, index);
}

Ext.data.Store.prototype.getJsonValues = function(){
    //delete any alement previously created
    var list = new Array();
    for(var i = 0; i < this.data.length; i++) {
        var record = this.getAt(i)
        var jsonValues = '{';
        var id = record.id > 0 ? record.id : '';
        jsonValues += "id:'" + id + "'";

        for(var j = 0; j< this.fields.items.length; j++){
            var field = this.fields.items[j];
            var value = ',' + field.name + ':';

            if(!field.type || field.type == '' || field.type == 'auto'){ // si no tiene tipo, lo mandamos como string
                value += "'" + record.get(field.name) + "'";
            }else if(field.type == 'string'){
                value += "'" + record.get(field.name) + "'";
            }else if(field.type == 'float'){
                value += record.get(field.name);
            }else if(field.type == 'date'){
                value += "'" + record.get(field.name).dateFormat('d/m/Y') + "'";
            }
            jsonValues += value;
        }
        jsonValues += '}';
        list.push(jsonValues);
    }
    return list;
}

Ext.grid.GridPanel.prototype.saveData = function(options){
    if(!options.form){
        options.form = Ext.DomHelper.append(this.el, {tag:'form', method:'post'}, true);
    }

    Ext.each(options.form.query('input[@name=' + options.inputName + ']'), function(e){
        Ext.get(e).remove();
    });

    var jsonValues = this.store.getJsonValues();

    Ext.each(jsonValues, function(value){
        Ext.DomHelper.append(options.form, {tag:'input', type:'hidden', name:options.inputName}, true).dom.value = value;
    });
}

Ext.data.Store.prototype.getFieldData = function(field){
    var list = new Array();
    for(var i = 0; i < this.data.length; i++) {
        list.push(this.getAt(i).get(field));
    }
    return list;
}

Ext.grid.GridPanel.prototype.saveFieldData = function(options){
    if(!options.form){
        options.form = Ext.DomHelper.append(this.el, {tag:'form', method:'post'}, true);
    }

    Ext.each(options.form.query('input[@name=' + options.inputName + ']'), function(e){
        Ext.get(e).remove();
    });

    var values = this.store.getFieldData(options.fieldName);

    Ext.each(values, function(value){
        var hidden = Ext.DomHelper.append(options.form, {tag:'input', type:'hidden', name:options.inputName}, true);
        hidden.dom.value = value;
    });
}

Ext.MessageBox.prompt = function(title, msg, fn, scope, multiline, value){
    this.show({
        title : title,
        msg : msg,
        buttons: this.OKCANCEL,
        fn: fn,
        minWidth:250,
        scope : scope,
        prompt:true,
        multiline: multiline,
        value: value
    });
    return this;
}

// le agrega el div para poner una descripcio
Ext.FormPanel.prototype.onRender = function(ct, position){
    this.initFields();
    Ext.FormPanel.superclass.onRender.call(this, ct, position);
    // le agrega el div para poner una descripcio
    if(this.description && this.description.length > 0){
	    this.body.createChild({tag:'div', cls:'form-desc', html:this.description});
    }
    this.form.initEl(this.body);
//    this.form.initEl(this.body.createChild(o));
}

// funciones para esconder todo el field con todo y etiquetas
Ext.override(Ext.form.Field, {

	// le agrega el div para poner una descripcio
	onRender : function(ct, position){
	    Ext.form.Field.superclass.onRender.call(this, ct, position);
	    if(!this.el){
	        var cfg = this.getAutoCreate();
	        if(!cfg.name){
	            cfg.name = this.name || this.id;
	        }
	        if(this.inputType){
	            cfg.type = this.inputType;
	        }
	        this.el = ct.createChild(cfg, position);
	    }
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

// se sobre escribe para soportar el que no muestre el campo de inicio.
Ext.apply(Ext.layout.FormLayout.prototype, {
    setContainer : function(ct){
        Ext.layout.FormLayout.superclass.setContainer.call(this, ct);
        this.container = ct;
        if(ct.labelAlign){
            ct.addClass('x-form-label-'+ct.labelAlign);
        }

        if(ct.hideLabels){
            this.labelStyle = "display:none";
            this.elementStyle = "padding-left:0;";
            this.labelAdjust = 0;
        }else{
            this.labelSeparator = ct.labelSeparator || this.labelSeparator;
            ct.labelWidth = ct.labelWidth || 100;
            if(typeof ct.labelWidth == 'number'){
                var pad = (typeof ct.labelPad == 'number' ? ct.labelPad : 5);
                this.labelAdjust = ct.labelWidth+pad;
                this.labelStyle = "width:"+ct.labelWidth+"px;";
                this.elementStyle = "padding-left:"+(ct.labelWidth+pad)+'px';
            }
            if(ct.labelAlign == 'top'){
                this.labelStyle = "width:auto;";
                this.labelAdjust = 0;
                this.elementStyle = "padding-left:0;";
            }
        }

        if(!this.fieldTpl){
            // the default field template used by all form layouts
            var t = new Ext.Template(
                '<div class="x-form-item {5}" tabIndex="-1">',
                    '<label for="{0}" style="{2}" class="x-form-item-label">{1}{4}</label>',
                    '<div class="x-form-element" id="x-form-el-{0}" style="{3}">',
                    '</div><div class="{6}"></div>',
                '</div>'
            );
            t.disableFormats = true;
            t.compile();
            Ext.layout.FormLayout.prototype.fieldTpl = t;
        }
    },
    renderItem:function(c, position, target){
        if(c && !c.rendered && (c.isFormField||c.fieldLabel) && c.inputType != 'hidden'){
            var args = [
                   c.id, c.fieldLabel,
                   c.labelStyle||this.labelStyle||'',
                   this.elementStyle||'',
                   typeof c.labelSeparator == 'undefined' ? this.labelSeparator : c.labelSeparator,
                   (c.itemCls||this.container.itemCls||'') + (c.hideLabel ? ' x-hide-label' : ''),
                   c.clearCls || 'x-form-clear-left'
            ];
            if(typeof position == 'number'){
                position = target.dom.childNodes[position] || null;
            }
            if(position){
                this.fieldTpl.insertBefore(position, args);
            }else{
                this.fieldTpl.append(target, args);
            }
            if(this.container.labelAlign == 'inside'){
                c.enableKeyEvents = true;
                c.setValue(c.fieldLabel);
                c.on('focus', function(field){
                    if(field.getValue() == field.fieldLabel){
                        field.selectText();
                    }
				});
                c.on('blur', function(field){
                    if(Ext.util.Format.trim(field.getValue()) == ''){
                        field.setValue(field.fieldLabel);
                    }
				});
                c.on('keyup', function(field, e){
                    // cambiar la clase a normal
                    if(Ext.util.Format.trim(field.getValue()) == ''){
                        field.setValue(field.fieldLabel);
                        field.selectText();
                    }
				});
            }
            c.render('x-form-el-'+c.id);
            // para no mostrar el campo
            if(c.renderHidden){
            	c.hideContainer();
            }

        }else {
            Ext.layout.FormLayout.superclass.renderItem.apply(this, arguments);
        }
    }
});

Ext.apply(Ext.tree.TreeNode.prototype, {
    destroy : function(){
        if(this.childNodes){
            for(var i = 0,l = this.childNodes.length; i < l; i++){
                this.childNodes[i].destroy();
            }
        }
        this.childNodes = null;
        if(this.ui.destroy){
            this.ui.destroy();
        }
    }
});

Ext.Window.prototype.closeAction = 'hide';


Ext.form.Action.Submit.prototype.getEntityId = function(){
    var root = this.response.responseXML.documentElement || this.response.responseXML;
    return Ext.DomQuery.selectValue('@entityId', root, true);
}

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

Ext.override(Ext.grid.GridView, {

        onRowOver : function(e, t){

            var row;

            if((row = this.findRowIndex(t)) !== false){

                this.grid.droprow=this.grid.getStore().getAt(row);

                this.addRowClass(row, 'x-grid3-row-over');

            }

        }

});

Ext.override(Ext.Editor, {
    beforeDestroy : function(){
        if(this.field){
            this.field.destroy();
            this.field = null;
        }
    }
});

function $FF(formId, fieldId){
    return Ext.getCmp(formId).form.findField(fieldId).getValue();
}