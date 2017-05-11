// test6.cc

#include <iostream>

using namespace std;

int main ()
{
  int q; int r; int x; int y;
  cin >> x;
  cin >> y;
  q = 0;
  r = x;
  while (r >= y) {
    q = q + 1;
    r = r - y;
  }
  cout << q;
  cout << r;
  return 0;
}
