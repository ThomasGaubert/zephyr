#include "overlaywidget.h"
#include "openvr.h"
#undef emit
#include <sio_client.h>
#include "openvroverlaycontroller.h"
#include <QApplication>
#include <QVariant>
#include <QJsonValue>
#include <QJsonDocument>
#include <QJsonObject>
#include <QVariantMap>
#include <QJsonArray>
#include <QFontDatabase>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    OverlayWidget *pOverlayWidget = new OverlayWidget;

    COpenVROverlayController::SharedInstance()->Init();

    COpenVROverlayController::SharedInstance()->SetWidget( pOverlayWidget );

    QFontDatabase::addApplicationFont(":/fonts/resources/Righteous-Regular.ttf");

    sio::client h;
    h.connect("http://localhost:3753");

    h.socket()->on("notification", sio::socket::event_listener_aux([&](std::string const& name, sio::message::ptr const& data, bool isAck, sio::message::list &ack_resp)
    {
        // Parse JSON
        QString str = QString::fromStdString(data->get_string());
        QJsonDocument jsonResponse = QJsonDocument::fromJson(str.toUtf8());
        QJsonObject jsonObj = jsonResponse.object();

        // Verify payload
        if(jsonObj["metadata"].toObject()["type"].toString() != "notification") {
           qWarning() << "Invalid payload type!";
           return;
        } else if(jsonObj["metadata"].toObject()["version"].toInt() != 1) {
           qWarning() << "Invalid payload version!";
           return;
        }
        std::string title = jsonObj["payload"].toObject()["title"].toString().toStdString();
        std::string msg = jsonObj["payload"].toObject()["text"].toString().toStdString();
        std::string notifText = title + "\n" + msg;

        qInfo() << "Notification: " << notifText.c_str();

        // Grab IVRNotifications interface
        vr::EVRInitError eError;
        vr::IVRNotifications * notif = (vr::IVRNotifications *) vr::VR_GetGenericInterface(vr::IVRNotifications_Version, &eError);
        vr::VROverlayHandle_t handle;
        vr::VROverlay()->FindOverlay("texasgamer.zephyr.overlay.Zephyr", &handle);

        /* TODO: Load notification icon
        QImage icon(qApp->applicationDirPath() + "/resources/overlay-icon.png", nullptr);
        vr::NotificationBitmap_t notifIcon;
        notifIcon.bytes = icon.bits();
        notifIcon.width = icon.width();
        notifIcon.height = icon.height();
        */

        // Create and display notification
        vr::VRNotificationId id;
        notif->CreateNotification(handle, 0, vr::EVRNotificationType_Transient, notifText.c_str(), vr::EVRNotificationStyle_Application, NULL, &id);
    }));

    return a.exec();
}
