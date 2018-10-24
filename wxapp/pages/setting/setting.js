const app = getApp(),
  open = "开启",
  close = "关闭"

Page({
  data: {
    setting: {},
    time: 0,
    timeItem: [
      {"text": open, "color": "#4b0"},
      {"text": close, "color": "#f44"}
    ],
    gift: 0,
    giftItem: [
      {"text": open, "color": "#4b0"},
      {"text": close, "color": "#f44"}
    ]
  },
  // 监听页面加载
  onLoad () {
    wx.request({
      url: app.serverIp + '/setting/queryById/' + app.sessionKey,
      method: 'GET',
      success: res => {
        this.setData({time: res.data.time, setting: res.data})
      }
    })
  },
  // 选择自动预测
  chooseAuto() {
    wx.showActionSheet({
      itemList: [open, close],
      success: res => {
        this.setData({time: res.tapIndex})
      }
    })
  },
  // 选择赠送库存
  chooseGift() {
    wx.showActionSheet({
      itemList: [open, close],
      success: res => {
        this.setData({gift: res.tapIndex})
      }
    })
  },
  // 表单提交按钮
  formSubmit(e) {
    let setting = e.detail.value
    setting.id = app.sessionKey
    setting.time = this.data.time
    setting.gift = this.data.gift
    wx.showLoading({title: '精彩马上继续'})
    // 提交
    wx.request({
      url: app.serverIp + '/setting/update',
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
      },
      data: JSON.stringify(setting),
      success: res => {
        app.resTip(res)
      }
    })
  }
})