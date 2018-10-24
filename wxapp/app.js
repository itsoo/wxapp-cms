App({
	/* 全局变量 */
  sessionKey: null,
  serverIp: 'https://xue2.net/wxapp-cms',
  myPhone: "18360450700",
  footerDetail: "©2018 BY MR.ZH PHONE: ",
  /* 全局函数 */
  // 提示层
  resTip(res) {
  	wx.hideLoading() // 隐藏加载
    if (res.data.success) {
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })
    } else {
      wx.showToast({
        title: '保存失败：错误原因可能网络或数据异常',
        icon: 'none',
        duration: 3000
      })
    }
  },
  // 分页查询
  queryListPage(object, pageNum) {
    let u = object.data.url,
        s = object.data.search
    wx.request({
      url: this.serverIp + u + pageNum + '-' + encodeURIComponent(s) + '-' + this.sessionKey,
      method: "GET",
      success: res => {
        object.setData({
          pageNum: res.data.pageNum,
          dataList: object.data.dataList.concat(res.data.dataList),
          hasNext: res.data.hasNext,
          loading: false
        })
      }
    })
  }
})