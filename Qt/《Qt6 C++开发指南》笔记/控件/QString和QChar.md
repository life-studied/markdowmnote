# QString和QChar

[TOC]

​	关键字：QString;QChar;utf16;unicode;asprintf;

---

​	`QString`的内置类型是`QChar`，是一个`utf16`格式的字符。`utf16`字符由2或4字节组成。

​	如果某个字符的编码超过65535，则使用4个字节存储，即在`QString`里用2个`QChar`存储。

## 1. 构造QChar

​	QChar有以下几种构造方法：

* 4位十六进制数字（最大0xffff）
* char16_t
* char
* 其它

```C++
QChar Hu = QChar(u'湖');
QChar ask = QCHar(0x10e3);		//？
QChar ask = QCHar(0x03a3);		//sigma符号
QChar charA = QChar('a');
```

## 2. QChar中的utf16字符

### 2.1 获取内含的utf16字符（unicode）

​	`unicode()`函数，返回`char16_t`字符；

```C++
char16_t unicodeChar = QChar("0x10e3").unicode();
```

### 2.2 将utf16字符转换为QChar（fromUcs2）

​	`QChar::fromUcs2()`函数，用于将`char16_t`转换为`QChar`；

```C++
QString str = "hello world";
QChar He = QChar::fromUcs2(str[0].unicode());
```

## 3. QChar——字符类型判断（isDigit/isLetter/...）

* 数字：isDigit()
* 字母：isLetter()
* 数字或字母：isLetterOrNumbera()
* 小写字母：isLower()
* 大写字母：isUpper()
* 空白符号：isSpace()
* 标点符号：isPunct()

少见：

* 标记：isMark()
* 标志：isSymbol()

```C++
void Widget::judgePlainTextCH(QChar ch)
{
    //判断特性
    ui->chkDigit->setChecked(ch.isDigit());
    ui->chkLetter->setChecked(ch.isLetter());
    ui->chkLetterOrNumber->setChecked(ch.isLetterOrNumber());
    ui->chkLower->setChecked(ch.isLower());
    ui->chkMark->setChecked(ch.isMark());
    ui->chkPunct->setChecked(ch.isPunct());
    ui->chkSpace->setChecked(ch.isSpace());
    ui->chkSymbol->setChecked(ch.isSymbol());
    ui->chkUpper->setChecked(ch.isUpper());
}
```

## 4. QString——格式字符串（asprintf）

​	`asprintf()`格式化字符串，并将结果存入`QString`。

```C++
QString str = QString::asprintf("\t, Unicode=0x%X",unicodeCh);
qDebug() << str;
```

## 5. QString.isEmpty()

​	判断字符串为空。

```C++
void Widget::on_btnCharJudge_clicked()
{
    //获取字符串
    QString str = ui->editChar->text();
    if(str.isEmpty()) return;
    
    //...
}
```

