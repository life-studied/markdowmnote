## ADD指令

格式:add 被加数,加数

影响位:

* 溢出OV（overflow，OF＝1）
* 增量UP（direction up，DF＝0）
* 允许中断EI（enable interrupt，IF＝1）
* 正PL（plus，SF＝0）
* 零ZR（zero，ZF＝1）
* 辅助进位AC（auxiliary carry，AF＝1）
* 偶校验PE（even parity，PF＝1）
* 进位CY（carry，CF＝1）