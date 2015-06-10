Ext.define('MgrPage.song.songlist', {
	extend: "Ext.Daemon.DaemonPanel",  
    requires: [
                
     ],
	alternateClassName: 'MgrSongListMain',
	   
	initComponent: function() {
		var me = this;
		me.hiddenTypeValue=null;
		var daemonFilePanel=null;
		var hiddenfield=Ext.create('Ext.form.field.Hidden', {xtype: 'hiddenfield',inputId:'id', name: 'id' });
		
		Ext.apply(me,{
			panelType:"grid",
			storeDefaultRootProperty:'listPageObject',
			panelTotalProperty:'recordCount',
			storeIdProperty:'code',
			formBodyStyle:'padding:15px 30% 15px 15px',
			showDelBtn:true,
			panelPageSize:36,
			delUrl:contextPath+'/beetlemgr/album/del',
			storeUrl:contextPath+"/mgr/songlist/list",
			formUrl:contextPath+'/mgr/songlist/saveOrUpdate',
			stateChangeUrl:contextPath+'/mgr/songlist/state',
		    listeners:{
		    	datapamel_selectionchange:function(selectionRowModel, records,  eOpts){
					if(records!=null&&records.length >0){
						// TODO
					}else{
						// TODO
					}
				},
		    	afterAddFormshow:function(theFormPage){
		    		hiddenfield.setValue("");
				},
				afterEditFormshow:function(theFormPage){
					var records = me.DaemonContentPanel.getSelectionModel().getSelection();
		    		if(records!=null&&records.length>0){
		    			hiddenfield.setValue(records[0].get("id"));
		    		}
				} 
		    },

		    storeFields:['id','name','keyword', 'img', 'icon','sortCode','typeName','state','createDate','createUser','updateUser','updateDate','remark','introduction','img'],
		    rightPanelToolBar:[MusicTreePickerToIndex,searchByName],
			panelColumns:[
		          {header:'id',dataIndex:'id',sortable:false,hidden:false,flex:0.5,renderer:null}
		         ,{header:'歌单名称',dataIndex:'name',sortable:false,hidden:false,flex:1,renderer:null}
		         ,{header:'歌单类别',dataIndex:'typeName',sortable:false,hidden:false,flex:1,renderer:null}
		         ,{header:'图标',dataIndex:'icon',sortable:false,hidden:false,flex:1,renderer:null}
			     ,{header:'创建日期',dataIndex:'createDate',sortable:false,hidden:false,flex:1,renderer:null}
			     ,{header:'简介',dataIndex:'introduction',sortable:false,hidden:false,flex:2,renderer:null}
		         ],
			daemonFormItems: [
			      hiddenfield	
			     ,{fieldLabel: '歌单名称',name: 'name',allowBlank: false,inputId:'name',minLength:1,maxLength:200,regexText:'1-200个字符',blankText:'1~200个字符',labelStyle: 'font-weight:bold;padding:0'}
				 ,{ xtype: 'textareafield',fieldLabel : '简介',inputId:'introduction',name:'introduction',style: 'margin:0' ,labelStyle: 'font-weight:bold;padding:0'} 
				 ,{ xtype: 'textareafield',fieldLabel : '备注',inputId:'remark',name:'remark',style: 'margin:0' ,labelStyle: 'font-weight:bold;padding:0'} 
				 ]
			});
		this.callParent(arguments);
	} 
});	