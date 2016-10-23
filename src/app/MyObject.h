#pragma once

#include <QtCore/QObject>

class MyObject : public QObject
{
  Q_OBJECT
public:
  explicit MyObject(QObject * parent = nullptr);

  void print();

};
