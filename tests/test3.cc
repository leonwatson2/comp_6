// test3.cc

// This program tests recursive functions.

#include <iostream>

using namespace std;

int facto (int x) {
  int s;
  if (x == 1)
    s = 1;
  else
    s = x * facto (x - 1);
  return s;
}

int main () {
  int i; int fac;
  {
    cin >> i;
    fac = facto (i);
  }
  cout << fac;
  return 0;
}