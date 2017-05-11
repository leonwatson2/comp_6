// test5.cc

// This program tests class definitions. 

#include <iostream>

using namespace std;

class List {

  public:

  int length;
  int max;
  int list [100];

  int initialize (int n) {
    int flag;
    int i;
    max = 100;
    if (n <= 0 || n > max)
      flag = 0;
    else 
      flag = n;
    return flag;
  }

  int getLength () { return length; }

  int nil () { 
    int nul;
    if (length > 0)
      nul = 0; 
    else
      nul = 1;
    return nul;
  }

  List cons (int a) {
    int i;
    List cons_list;
    cons_list . length = cons_list . initialize (length + 1);
    if (cons_list . length > 0)  {
      cons_list . list [0] = a;
      i = 0;
      while (i < length) {
        cons_list . list [i + 1] = list [i];
        i = i + 1;
      }
    }
    return cons_list;
  }

  int head () { 
    int head;
    if (length == 0) 
      head = 0;
    else
      head = list [0];
    return head;
  }

  List tail () {
    int i;
    List tail_list;
    i = tail_list . initialize (length - 1);
    while (i > 0) {
      tail_list = tail_list . cons (list [i]);
       i = i - 1;
    }
    return tail_list;
  }

  int equals (List list2) {
    int equals;
    int i;
    if (length != list2 . length)
      equals = 0;
    else {
      equals = 1;
      i = 0;
      while (i < length && list [i] == list2 . list [i])
  i = i + 1;
      if (i < length) equals = 0;
    }
    return equals;
  }

  int print () {
    int i;
    cout << 88888;
    i = 0;
    while (i < length) {
      cout << list [i];
      i = i + 1;
    }
    cout << 88888;
    return length;
  }
};


int main () {
  int i; 
  List l1; List l2;
  List my_list;
  cout << (my_list . nil ());
  i = my_list . initialize (20);
  i = 10;
  while (i > 0) {
    my_list = my_list . cons (i);
    i = i - 1;
  }
  i = my_list . print ();
  if (my_list . nil ())
    cout << 1;
  else
    cout << 0;
  cout << (my_list . getLength ());
  cout << (my_list . head ());
  i = my_list . tail () . print ();
  l1 = my_list . cons (0);    
  l2 = my_list . cons (0);
  if (l1 . equals (l2))
    cout << 1;
  else 
    cout << 0;
  if (my_list . equals (l1)) 
    cout << 1;
  else 
    cout << 0;
  return 0;
}
