// test4.cc

// This program tests arrays passed as parameters.

#include <iostream>

using namespace std;

int f (int a [100], int i) {
  int x;
  x = a [i];
  a [i] = 0;
  i = 622;
  return x;
}

int main ()
{
  int b[100];
  int i; int n;
  n = 40;
  i = 0;
  while (i < 100) {
    b [i] = i * i;
    i = i + 1;
  }
  cout << n;
  cout << f (b, n);
  cout << n;   // n should be unchanged
  cout << b [n]; // b [n] should be 0
  return 0;
}
