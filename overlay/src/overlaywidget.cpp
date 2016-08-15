#include "overlaywidget.h"
#include "ui_overlaywidget.h"
#include "openvr.h"

OverlayWidget::OverlayWidget(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::OverlayWidget)
{
    ui->setupUi(this);
}

OverlayWidget::~OverlayWidget()
{
    delete ui;
}

void OverlayWidget::on_quitBtn_clicked()
{
    QApplication::quit();
}

void OverlayWidget::on_notifyBtn_clicked()
{
    vr::EVRInitError eError;
    vr::IVRNotifications * notif = ( vr::IVRNotifications * ) vr::VR_GetGenericInterface(vr::IVRNotifications_Version, &eError);

    vr::VROverlayHandle_t overlay;
    vr::VROverlay()->FindOverlay("texasgamer.zephyr.overlay.Alerts", &overlay);
    vr::VRNotificationId id;
    notif->CreateNotification(overlay, 0, vr::EVRNotificationType_Transient, "Alert", vr::EVRNotificationStyle_Application, NULL, &id);
}
