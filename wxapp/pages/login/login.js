//获取应用实例
const app = getApp()

Page({
  // 初始数据
  data: {
    errorMsg: '请升级微信版本',
    footerDetail: app.footerDetail,
    myPhone: app.myPhone,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  // 用户点击登录
  bindGetUserInfo () {
    // 登录
    wx.login({
      success: res => {
        let jsCode = res.code
        // 获取用户信息
        wx.getUserInfo({
          success: res => {
            // 发送登录请求
            wx.request({
              url: app.serverIp + '/user/login',
              method: 'POST',
              header: {
                'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
              },
              data: JSON.stringify({
                jsCode: jsCode,
                encryptedData: res.encryptedData,
                iv: res.iv
              }),
              method: "POST",
              success: res => {
                let sk = res.data.sessionKey,
                    em = res.data.errorMsg
                if (sk && em) { // 新增用户
                  this.setData({canIUse: false, errorMsg: `感谢使用，您的试用期为 ${em} 天`})
                  app.sessionKey = sk
                  var tt = new Date(new Date().getTime() + (em * 24 * 3600000)); // 到期时间
                  wx.setStorageSync('tryoutTime', tt)
                  wx.setStorageSync('sessionKey', sk)
                  setTimeout(function() {
                    wx.redirectTo({url: '../../pages/index/index'})
                  }, 1500)
                } else if (!sk && em) { // 已过期
                  wx.removeStorageSync('tryoutTime')
                  wx.removeStorageSync('sessionKey')
                  this.setData({canIUse: false, errorMsg: '已过期，请联系 ' + app.myPhone})
                } else { // 正常登录
                  app.sessionKey = sk
                  wx.removeStorageSync('tryoutTime')
                  wx.setStorageSync('sessionKey', sk)
                  wx.redirectTo({url: '../../pages/index/index'})
                }
              }
            })
          }
        })
      }
    })
  }
})