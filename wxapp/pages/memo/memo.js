const app = getApp()

Page({
  // 初始数据
  data: {
    user: {}
  },
  // 页面加载
  onLoad() {
    wx.request({
      url: app.serverIp + '/user/queryMemo/' + app.sessionKey,
      method: 'GET',
      success: res => {
        let user = this.data.user
        user.memo = res.data.memo
        this.setData({user})
      }
    })
  },
  // 表单提交按钮
  formSubmit(e) {
    let user = this.data.user
    user.id = app.sessionKey
    user.memo = e.detail.value.memo
    wx.request({
      url: app.serverIp + '/user/saveMemo',
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
      },
      data: JSON.stringify(user),
      success: res => {
        app.resTip(res)
      }
    })
  }
})