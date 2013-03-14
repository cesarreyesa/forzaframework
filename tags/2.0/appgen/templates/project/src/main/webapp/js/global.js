
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

/* This function is used to open a pop-up window */
function openWindow(url, winTitle, winParams) {
    var win = window.open(url, winTitle, winParams);
    if(!win.opener) win.opener = self;
    win.focus();
}

function openCenterWindow(url, winTitle, winParams, width, height) {
    var left;
    var top;
    var screenWidth = screen.availWidth;
    var screenHeight = screen.availHeight;

    width = screenWidth - (screenWidth - width);
    height = screenHeight - (screenHeight - height);
    left = (screenWidth-width)/2;
    top = (screenHeight-height)/2;
    winParams += ",width=" + width + ",height=" + height + ",left=" + left + ",top=" + top;

    win = window.open(url, winTitle, winParams);
    if(!win.opener) win.opener = self;
    win.focus();
}


/* This function is used to set cookies */
function setCookie(name, value, expires, path, domain, secure) {
    document.cookie = name + "=" + escape(value) +
                      ((expires) ? "; expires=" + expires.toGMTString() : "") +
                      ((path) ? "; path=" + path : "") +
                      ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
}

/* This function is used to get cookies */
function getCookie(name) {
    var prefix = name + "="
    var start = document.cookie.indexOf(prefix)

    if (start == -1) {
        return null;
    }

    var end = document.cookie.indexOf(";", start + prefix.length)
    if (end == -1) {
        end = document.cookie.length;
    }

    var value = document.cookie.substring(start + prefix.length, end)
    return unescape(value);
}

/* This function is used to delete cookies */
function deleteCookie(name, path, domain) {
    if (getCookie(name)) {
        document.cookie = name + "=" +
                          ((path) ? "; path=" + path : "") +
                          ((domain) ? "; domain=" + domain : "") +
                          "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}

function navigate(url) {
    document.location.href = url;
}

function isAllDigitsFloat(s) {
    var test = "" + s;
    for (var k = 0; k < test.length; k++) {
        var c = test.substring(k, k + 1);
        if (isDigit(c) == false) {
            if (c != ".")
                return false;
        }
    }
    return true;
}

function isDigit(num) {
    if (num.length > 1) {
        return false;
    }
    var string = "1234567890";
    if (string.indexOf(num) != -1) {
        return true;
    }
    return false;
}

function isGtZero(num, msg) {
    var isValid = false;
    if (parseFloat(num) <= 0) {
        isValid = true;
        alert(msg);
    }
    return isValid;
}

function isValidTime(time) {
    var str = new String(trim(time));
    var pattern = /^(\d{1,2}):(\d{2})$/;
    var matchArray = str.match(pattern);
    if (matchArray == null) {
        alert("Time is not in a valid format.");
        return false;
    }
    hour = matchArray[1];
    minute = matchArray[2];

    if (hour < 0 || hour > 23) {
        alert("Hour must be between 1 and 23 for military time)");
        return false;
    }

    if (minute < 0 || minute > 59) {
        alert("Minute must be between 0 and 59.");
        return false;
    }
    return true;
}

function doDelete(id, url, callback){
    Ext.Ajax.request({url: url, callback: callback, params: {id: id, mode: 'delete'}});
}

function formatDate2(value){
    return value ? value.dateFormat('M d, Y') : '';
}

function parseDate(value){
    return Date.parseDate(value, 'd/m/Y');
}

function parseBoolean(value){
    return new Boolean(value);
}

function createDocument(){
    var doc;
    if (document.implementation && document.implementation.createDocument)
    {
        doc = document.implementation.createDocument("", "", null);
    }
    else if (window.ActiveXObject)
    {
        doc = new ActiveXObject("Microsoft.XMLDOM");
    }
    return doc;
}

var Ext = {};
if(!Nopal){
    var Nopal = {};
}

Nopal.Ajax = {
    request : function(options){
        if(options.btn){
            options.btn.disable();
        }
        if(options.statusbar){
            options.statusbar.showBusy();
        }
        if(options.longOperation){
            Ext.Ajax.timeout = 6000000;
        }
        Ext.Ajax.request({
            url: options.url,
            form: options.form,
            method: options.method || 'POST',
            params: options.params,
            isUpload: options.isUpload,
            success: this.processResponse,
            failure: this.processException,
            argument: {success: options.success, failure : options.failure, btn : options.btn, statusbar: options.statusbar, longOperation: options.longOperation}
        });
    },

    execute : function(url, params, success, failure){
        Ext.Ajax.request({
            url: url,
            method:'POST',
            params: params,
            success: this.processResponse,
            failure: this.processException,
            argument: {success: success, failure : failure}
        });
    },

    executeForm : function(url, form, params, success, failure){
        Ext.Ajax.request({
            url: url,
            form: form,
            params: params,
            success: this.processResponse,
            failure: this.processException,
            argument: {success: success, failure : failure}
        });
    },

    processResponse : function(response) {
        if(response.argument.longOperation){
            Ext.Ajax.timeout = 30000;
        }
        var callback;
        var jsonObj = Ext.util.JSON.decode("(" + response.responseText + ")");
        try {
            if(jsonObj.success){
                callback = response.argument.success;
                if (typeof callback == 'function') {
                    callback(jsonObj);
                }
            }
            else{
                callback = response.argument.failure;
                if (typeof callback == 'function') {
                    callback(jsonObj, response);
                }else{
                    Nopal.Ajax.processErrors(jsonObj);
                }
            }
        } catch(e) {
            callback = response.argument.failure;
            if (typeof callback == 'function') {
                callback(jsonObj, response);
            }
            else{
                Ext.Msg.alert('Error', e);
            }
        }
        var btn = response.argument.btn;
        if(btn){
            btn.enable();
        }
        if(response.argument.statusbar){
            response.argument.statusbar.clearStatus();
        }
    },

    processErrors : function(info, btn){
        var errors = '';
        if(info.fields){
            Ext.each(info.fields, function(field){
                Ext.each(field.errors, function(error){
                    errors += error.message + '<br/>';
                });
                errors += '<br />';
            });
        }else{
            Ext.each(info.errors, function(error){
                errors += error.message + '<br/>';
            });
        }
        Ext.Msg.alert('Error', errors);
    },

    processException: function(response){
//        Ext.MessageBox.hide();
        if(response.argument.btn){
            response.argument.btn.enable();
        }
        if(response.argument.statusbar){
            response.argument.statusbar.clearStatus();
        }

        var text = response.responseText && response.responseText.length > 500 ? response.responseText.substr(0, 500) : response.responseText;
        Ext.Msg.alert("Error", "There has an error. Please try again.<br/>Status: " + response.status + "" +
                               "<br />Status Text: " + text + "<br/>");
    }
}
