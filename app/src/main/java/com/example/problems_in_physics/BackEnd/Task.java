package com.example.problems_in_physics.BackEnd;

import com.example.problems_in_physics.BackEnd.Body;
import com.example.problems_in_physics.BackEnd.Force;
import com.example.problems_in_physics.BackEnd.HingedConnection;
import com.example.problems_in_physics.BackEnd.Point;

import java.io.Serializable;
import java.util.ArrayList;

// З А Д А Н И Е   С И Л
//
// Можем задать внешнюю силу: известную, неизвестную и с неизвестным углом,
// и три типа связей: шарнирная связь, шарнирная опора и гладкая опора.
// _
// Неизвестная сила с одной известной проекцией задаётся как известная сила
// (равна проекции) плюс неизвестная сила с известным углом (вдоль другой оси).
// _
// Сила, приложенная к шарнирному соединению, задаётся как сила, приложенная
// лишь к одному из тел в этом соединении.
//
// todo:
//  Можно добавить шершавую опору: две силы, про одну известен угол (нормали), а
//  другая связана с первой коэффициентом трения покоя.
//  _
//  Можно добавить известный момент: эквивалентно двум равным силам,
//  приложенным к разным точкам.
//  ПРОБЛЕМА: эти силы не должны быть показаны пользователю в ответе.
//  Также можно добавить неизвестный момент.
//  _
//  Можно добавить жёсткую заделку: шарнирная опора плюс неизвестный момент.
//  _
//  Можно добавить скользящую заделку: гладкая опора плюс неизвестный момент.
//  _
//  Можно добавить бискользящую заделку: неизвестный момент.
//  _
//  Можно добавить жёсткую заделку: шарнирная опора плюс неизвестный момент.
//  _
//  Можно добавить опорное соединение (см. первый рисунок в первом файле
//  с задачами): две равных неизвестных силы с известными
//  (противоположными) углами (нормали). Здесь же можно добавить трение.

public  class  Task implements Serializable {
  private static int variables_num = 0;
  private ArrayList<Body> bodies = new ArrayList<>();
  private ArrayList<HingedConnection> hinged_connections = new ArrayList<>();
  
  public void setBodies(int num) {
    for (int i = 0; i < num; i++) {
      bodies.add(new Body());
    }
  }
  
  public ArrayList<Body> getBodies() {
    return bodies;
  }

  public void addKnownForce(int body_index, Point point,
                            double value, double angle) {
    bodies.get(body_index).addForce(new Force(body_index, point, value, angle));
  }
  
  public void addUnknownForceWithKnownAngle(int body_index, Point point,
                                     double angle) {
    bodies.get(body_index).addForce(new Force(body_index, point, angle,
        variables_num + 1, variables_num + 2));
    variables_num += 2;
  }
  
  public void addUnknownForce(int body_index, Point point) {
    bodies.get(body_index).addForce(new Force(body_index, point,
        variables_num + 1, variables_num + 2));
    variables_num += 2;
  }

  public void addHingedConnection(int body_1_index, int body_2_index,
                                  Point point) {
    Force force_1 = new Force(body_1_index, point,
        variables_num + 1, variables_num + 2);
    Force force_2 = new Force(body_2_index, point,
        variables_num + 3, variables_num + 4);
    bodies.get(body_1_index).addForce(force_1);
    bodies.get(body_2_index).addForce(force_2);
    variables_num += 4;
    hinged_connections.add(new HingedConnection(force_1, force_2));
  }

  public void addHingedSupport(int body_index, Point point) {
    addUnknownForce(body_index, point);
  }

  public void addSmoothSupport(int body_index,
                               Point point, double normal_angle) {
    addUnknownForceWithKnownAngle(body_index, point, normal_angle);
  }
  
  public ArrayList<HingedConnection> getHingedConnections() {
    return hinged_connections;
  }
  
  public static int getVariablesNum() { return variables_num; }
}
