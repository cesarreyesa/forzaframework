Ext.Updater.defaults.loadScripts = true;

Ext.Window.prototype.closeAction = 'hide';

Ext.Ajax.on('beforerequest', function(conn, options){
    options.url += options.url.indexOf('?') < 0 ? '?d=nd' : '&d=nd';
});

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

// Acepta un url y crean un proxy nuevo/
Ext.data.Store.prototype.reload = function(options){
    if(options && options.url){
        this.proxy = new Ext.data.HttpProxy({ url: options.url});
    }
    this.load(Ext.applyIf(options||{}, this.lastOptions));
}

Ext.data.GroupingStore.prototype.applySort = function(){
    Ext.data.GroupingStore.superclass.applySort.call(this);
    if(!this.groupOnSort && !this.remoteGroup){
        var gs = this.getGroupState();
        if(gs && this.sortInfo && (gs != this.sortInfo.field || this.groupDir != this.sortInfo.direction)){
            this.sortData(this.groupField, this.groupDir);
        }
    }
}

/*
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
*/


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

Ext.form.Action.Submit.prototype.getEntityId = function(){
    var root = this.response.responseXML.documentElement || this.response.responseXML;
    return Ext.DomQuery.selectValue('@entityId', root, true);
}

//// UX ////


