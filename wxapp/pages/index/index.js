const app = getApp()

Page({
  data: {
    footerDetail: app.footerDetail,
    myPhone: app.myPhone,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    showed: false,
    search: '',
    bar: {
      showed: true,
      text: '欢迎使用 WXAPP-CMS 1.0 版本目前已正式上线，请点击 [帮助手册] 查看已开放的功能。',
      scrollable: true
    },
    warnText: '',
    memoWarn: true
  },
  showInput() {
    this.setData({showed: true})
  },
  hideInput() {
    this.setData({showed: false})
  },
  inputTyping(e) {
    this.setData({search: e.detail.value})
  },
  // 页面加载
  onLoad() {
    // 校验登录
    let sk = wx.getStorageSync('sessionKey'),
      tt = wx.getStorageSync('tryoutTime'),
      days = 1
    if (tt) {
      days = parseInt((tt - new Date()) / (24 * 3600000))
    }
    if (!sk || days < 0) {
      wx.redirectTo({url: '../../pages/login/login'})
    }
    app.sessionKey = sk
  },
  // 搜索客户信息
  doSearch() {
    let search = this.data.search
    if (search) {
      wx.navigateTo({url: '../quick/quick?search=' + search})
    }
  },
  // 页面显示
  onShow() {
    // 首页提醒加载
    wx.request({
      url: app.serverIp + '/allWarn/' + app.sessionKey,
      method: 'GET',
      success: res => {
        let data = res.data
        this.setData({
          warnText: data.warnText,
          memoWarn: data.memoWarn
        })
      }
    })
  }
})