const app = getApp()

Page({
  // 初始数据
  data: {
    store: {}
  },
  // 监听页面加载
  onLoad () {
    wx.request({
      url: app.serverIp + '/store/queryById/' + app.sessionKey,
      method: 'GET',
      success: res => {
        let store = res.data
        if (store) {
          this.setData({store: store})
        }
      }
    })
  },
  // 表单提交按钮
  formSubmit(e) {
    let store = e.detail.value
    store.id = app.sessionKey
    store['now_num'] = this.data.store['now_num']
    wx.showLoading({title: '精彩马上继续'})
    // 提交
    wx.request({
      url: app.serverIp + '/store/update',
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
      },
      data: JSON.stringify(store),
      success: res => {
        app.resTip(res)
      }
    })
  }
})