#include "MyObject.h"

#include <iostream>

MyObject::MyObject(QObject * parent)
  : QObject(parent)
{}

void MyObject::print()
{
    std::cout << "Bu-ga-ga" << std::endl;
}
