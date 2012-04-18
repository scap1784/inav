#include<iostream>
#include<string>
#include"../whois.h"

using namespace std;

int main(int argc, char * argv[])
{
  Whois whois;
//  string ip = "google.com";
//  string output = whois.whois_Lookup(ip);
//  unsigned int numIP = 3232235777U;

  for(int i = 1; i < argc; i++)
  {
    int A,B,C,D;
    sscanf(argv[i], "%d.%d.%d.%d", &A, &B, &C, &D);
    unsigned int ip = 0;
    ip = (A << 24) | (B << 16) | (C << 8) | D;
    string output = whois.whois_Lookup(ip);
    cout << "Output: " << endl << output << endl;

  }
  //string output = whois.whois_Lookup(numIP);
  //cout << "Output: " << endl << output << endl;

  return 0;
}
