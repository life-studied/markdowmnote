import re
import sys
f=open('./makefile','r')
alllines=f.readlines()
f.close()
f=open('./makefile','w+')

sysname = sys.platform
if sysname == "win32":
    for eachline in alllines:
        a=re.sub('/',r'\\',eachline)
        f.writelines(a)
elif sysname == "linux":
    for eachline in alllines:
        a=re.sub(r'\\','/',eachline)
        f.writelines(a)
f.close()