const app = getApp(),
  depositList = ['未交', '已交', '已退'],
  goodsList = ['未留', '已留', '收回']

Page({
  data: {
    isShow: false,
    deposit: 0,
    goods: 0,
    id: 0,
    custom: {},
    label: [],
    addLabel: '',
    hasDeposit: [{ // 押金
      'text': depositList[0],
      'color': '#999'
    }, {
      'text': depositList[1],
      'color': '#4b0'
    }, {
      'text': depositList[2],
      'color': '#f44'
    }],
    hasGoods: [{ // 空桶
      'text': goodsList[0],
      'color': '#999'
    }, {
      'text': goodsList[1],
      'color': '#4b0'
    }, {
      'text': goodsList[2],
      'color': '#f44'
    }]
  },
  // 页面加载
  onLoad(options) {
    let para = ''
    if (options.id) {
      wx.request({
        url: app.serverIp + '/custom/queryById/' + options.id,
        method: 'GET',
        success: res => {
          let custom = res.data
          this.setData({
            id: custom.id,
            custom: custom,
            label: custom.Label,
            deposit: custom.deposit,
            goods: custom.goods
          })
        }
      })
      wx.setNavigationBarTitle({title: '编辑客户'})
    } else {
      wx.setNavigationBarTitle({title: '新增客户'})
    }
  },
  // 选择押金
  chooseDeposit() {
    wx.showActionSheet({
      itemList: depositList,
      success: res => {
        this.setData({deposit: res.tapIndex})
      }
    })
  },
  // 选择空桶
  chooseGoods() {
    wx.showActionSheet({
      itemList: goodsList,
      success: res => {
        this.setData({goods: res.tapIndex})
      }
    })
  },
  // 输入标签
  inputLabling(e) {
    this.setData({addLabel: e.detail.value})
  },
  // 保存标签
  addLabel() {
    let addLabel = this.data.addLabel.replace(/(^\s*)|(\s*$)/g, '')
    if (addLabel != '') {
      let label = this.data.label
      label.push(addLabel)
      this.setData({label: label})
    }
    this.setData({addLabel: ''})
  },
  // 移除标签
  removeLabel(e) {
    let label = this.data.label
    label.splice(e.target.dataset.i, 1)
    this.setData({label: label})
  },
  // 表单提交按钮
  formSubmit(e) {
    let data = this.data,
      custom = data.custom
    custom = e.detail.value
    if (data.id != 0) {
      custom.id = data.id
    }
    custom.user = app.sessionKey
    custom.deposit = data.deposit
    custom.goods = data.goods
    custom.Label = data.label
    // 有效性校验
    let validate = ['name', 'phone', 'address', 'price']
    for (let v in validate) {
      if (custom[validate[v]] == '') {
        this.setData({isShow: true})
        setTimeout(() => {
          this.setData({isShow: false})
        }, 2500)
        return
      }
    }
    wx.showLoading({title: '精彩马上继续'})
    // 提交
    wx.request({
      url: app.serverIp + '/custom/save',
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
      },
      data: JSON.stringify(custom),
      success: res => {
        app.resTip(res)
      }
    })
  }
})