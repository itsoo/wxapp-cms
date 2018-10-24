const app = getApp()

Page({
	callMe() {
		wx.makePhoneCall({phoneNumber: app.myPhone})
	}
})