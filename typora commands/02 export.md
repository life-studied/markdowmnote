# export

## 导出命令

```js
ClientCommand.export({"type": "pdf", "appendBody": "", "runCommand": false, "runCommandStr": "", "showOutput": false, "key": "pdf"});
```

## frame.js相关解读

```js
c.export = function (e) {
            var t = e.type;
            i.editor.export.beforeExport(),
                i.editor.export.showExportNotification(),
                i.editor.localSettingBridge.loadFromFile(),
                e = i.editor.localSettingBridge.loadExportOption(e),
                "html" == t ? i.editor.export.exportAndSaveHTML(e) : 
                "html-plain" == t ? (Object.assign(e, { noStyle: !0 }), i.editor.export.exportAndSaveHTML(e)) : 
                "image" == t ? i.editor.export.exportToImage(e) : 
                "pdf" == t ? s.exportPDF(e) : 
                "custom" == t ? i.editor.export.exportWithCommand(e) : 
                l.export(t, e), /^html/.exec(t) && document.body.classList.remove("ty-show-notification")
        }, 
```

