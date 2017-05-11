// test2.cc

// This program tests non-local referencing.

#include <iostream>

using namespace std;

int h;

int area (int x, int y) {
  int z;
  z = 2 * (x * y + (x * h) + y * h);
  return z;
}


int main () {
  int a; int b; int s;
  {
    cin >> a;
    cin >> b;
    cin >> h;
    s = area (a, b);
  }
  cout << s;
  return 0;
}