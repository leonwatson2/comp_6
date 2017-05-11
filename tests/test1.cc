// test1.cc

#include <iostream>

using namespace std;

int main () {
  int my_list [100]; 
  int my_list_tl [100];
  int r; int h; int i;
  {
    r = 2;
    while (r < 5) {  
      my_list [r - 2] = r;
      r = r + 1;
    }  
    h = my_list [0];
    i = r;
    while (i > 0) {
      my_list_tl [i - 1] = my_list [i];
      i = i - 1;
    }
  }
  cout << h; 
  cout << my_list_tl [0];
  return 0;
}
