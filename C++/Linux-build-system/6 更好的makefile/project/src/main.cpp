#include"add.h"
#include"min.h"
#include<iostream>
using namespace std;
int main(int argc,char* argv[])
{
    int a{};
    a = 1;
    a++;
    a++;
    a = add(1,3);
    int b = min(2,2);
    cout<<a<<b<<endl;
    return 0;
}