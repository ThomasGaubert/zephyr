QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = openvr-notifications
TEMPLATE = app

SOURCES += main.cpp\
        overlaywidget.cpp \
    openvroverlaycontroller.cpp \
    sio_client.cpp \
    sio_socket.cpp \
    internal/sio_client_impl.cpp \
    internal/sio_packet.cpp

HEADERS  += overlaywidget.h \
    openvroverlaycontroller.h

FORMS    += overlaywidget.ui

INCLUDEPATH += ../headers
INCLUDEPATH += ../../../socketio-cpp/lib/websocketpp
INCLUDEPATH += ../../../socketio-cpp/lib/rapidjson/include

LIBS += -L../lib/win32 -lopenvr_api -lboost

DESTDIR = ../bin/win32

CONFIG+=c++11
