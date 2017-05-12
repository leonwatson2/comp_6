// test6.cc

#include <iostream>

using namespace std;

int main ()
{
  int q; int r; int x; int y;
  q = r + 1;
  r = x + q;

  q = x + y;
  if(q < r){
    r = r + 1;
  }
  cout << q;
  return 0;
}
