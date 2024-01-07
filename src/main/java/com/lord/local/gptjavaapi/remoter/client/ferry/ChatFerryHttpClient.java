package com.lord.local.gptjavaapi.remoter.client.ferry;

import com.lord.local.gptjavaapi.config.OpenAiChatFeignConfig;
import com.lord.local.gptjavaapi.remoter.model.ferry.BodySendPatMsgPatPostRequest;
import com.lord.local.gptjavaapi.remoter.model.ferry.BodySendTextTextPostRequest;
import com.lord.local.gptjavaapi.remoter.model.ferry.HTTPValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
@FeignClient(name = "chatFerry", url = "http://192.168.199.129:9999",contextId = "ChatFerryHttpClient",configuration = OpenAiChatFeignConfig.class)
public interface ChatFerryHttpClient {

    @Operation(summary = "通过好友申请", description = "通过好友申请  Args:     v3 (str): 加密用户名 (好友申请消息里 v3 开头的字符串)     v4 (str): Ticket (好友申请消息里 v4 开头的字符串)     scene: 申请方式 (好友申请消息里的 scene)  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/new-friend",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> acceptNewFriendNewFriendPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "添加群成员", description = "添加群成员  Args:     roomid (str): 待加群的 id     wxids (str): 要加到群里的 wxid，多个用逗号分隔  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/chatroom-member",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> addChatroomMembersChatroomMemberPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "（废弃）解密图片", description = "解密图片:  Args:     src (str): 加密的图片路径     dst (str): 解密的图片路径  Returns:     bool: 是否成功", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/dec-image",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> decryptImageDecImagePost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "删除群成员", description = "删除群成员  Args:     roomid (str): 群的 id     wxids (str): 要删除的 wxid，多个用逗号分隔  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/chatroom-member",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Object> delChatroomMembersChatroomMemberDelete(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "（废弃）下载图片、文件和视频", description = "下载附件（图片、视频、文件）  Args:     id (int): 消息中 id     thumb (str): 消息中的 thumb     extra (str): 消息中的 extra  Returns:     str: 成功返回存储路径；空字符串为失败，原因见日志。", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/attachment",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> downloadAttachmentAttachmentPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "下载图片", description = "下载图片  Args:     id (int): 消息中 id     extra (str): 消息中的 extra     dir (str): 存放图片的目录     timeout (int): 超时时间（秒）  Returns:     str: 成功返回存储路径；空字符串为失败，原因见日志。", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/save-image",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> downloadImageSaveImagePost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "获取群成员名片", description = "获取群成员名片  Args:     roomid (str): 群的 id     wxid (str): wxid  Returns:     str: 名片", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/alias-in-chatroom",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getAliasInChatroomAliasInChatroomGet(@Parameter(in = ParameterIn.QUERY, description = "wxid" ,schema=@Schema( defaultValue="wxid_xxxxxxxxxxxxx")) @Valid @RequestParam(value = "wxid", required = false, defaultValue="wxid_xxxxxxxxxxxxx") Optional<Object> wxid
            , @Parameter(in = ParameterIn.QUERY, description = "群的 id" ,schema=@Schema( defaultValue="xxxxxxxx@chatroom")) @Valid @RequestParam(value = "roomid", required = false, defaultValue="xxxxxxxx@chatroom") Optional<Object> roomid
    );


    @Operation(summary = "保存语音", description = "保存语音  Args:     id (int): 消息中 id     dir (str): 存放图片的目录     timeout (int): 超时时间（秒）  Returns:     str: 成功返回存储路径；空字符串为失败，原因见日志。", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/save-audio",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> getAudioMsgSaveAudioPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "获取群成员", description = "获取群成员  Args:     roomid (str): 群的 id  Returns:     List[dict]: 群成员列表", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/chatroom-member",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getChatroomMembersChatroomMemberGet(@Parameter(in = ParameterIn.QUERY, description = "群的 id" ,schema=@Schema( defaultValue="xxxxxxxx@chatroom")) @Valid @RequestParam(value = "roomid", required = false, defaultValue="xxxxxxxx@chatroom") Optional<Object> roomid
    );


    @Operation(summary = "获取完整通讯录", description = "获取完整通讯录", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/contacts",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getContactsContactsGet();


    @Operation(summary = "获取所有数据库", description = "获取所有数据库", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/dbs",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getDbsDbsGet();


    @Operation(summary = "获取好友列表", description = "获取好友列表", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/friends",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getFriendsFriendsGet();


    @Operation(summary = "获取消息类型", description = "获取消息类型", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/msg-types",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getMsgTypesMsgTypesGet();


    @Operation(summary = "获取 OCR 结果", description = "获取 OCR 结果。鸡肋，需要图片能自动下载；通过下载接口下载的图片无法识别。  Args:     extra (str): 待识别的图片路径，消息里的 extra  Returns:     str: OCR 结果", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/ocr-result",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getOcrResultOcrResultGet(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "获取登录账号 wxid", description = "获取登录账号 wxid", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/wxid",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getSelfWxidWxidGet();


    @Operation(summary = "获取 db 中所有表", description = "获取 db 中所有表  Args:     db (str): 数据库名（可通过 `get_dbs` 查询）  Returns:     List[dict]: `db` 下的所有表名及对应建表语句", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/{db}/tables",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getTablesDbTablesGet(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("db") Object db
    );


    @Operation(summary = "获取登录账号个人信息", description = "获取登录账号个人信息", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/user-info",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> getUserInfoUserInfoGet();


    @Operation(summary = "邀请群成员", description = "邀请群成员  Args:     roomid (str): 待加群的 id     wxids (str): 要加到群里的 wxid，多个用逗号分隔  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/cr-members",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> inviteChatroomMembersCrMembersPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "获取登录状态", description = "获取登录状态", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))) })
    @RequestMapping(value = "/login",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> isLoginLoginGet();


    @Operation(summary = "执行 SQL，如果数据量大注意分页，以免 OOM", description = "执行 SQL，如果数据量大注意分页，以免 OOM  Args:     db (str): 要查询的数据库     sql (str): 要执行的 SQL  Returns:     List[dict]: 查询结果", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/sql",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> querySqlSqlPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "接收转账", description = "接收转账  Args:     wxid (str): 转账消息里的发送人 wxid     transferid (str): 转账消息里的 transferid     transactionid (str): 转账消息里的 transactionid  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/transfer",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> receiveTransferTransferPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "刷新朋友圈（数据从消息回调中查看）", description = "刷新朋友圈  Args:     id (int): 开始 id，0 为最新页  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/pyq",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Object> refreshPyqPyqGet(@Parameter(in = ParameterIn.QUERY, description = "开始 id，0 为最新页" ,schema=@Schema( defaultValue="0")) @Valid @RequestParam(value = "id", required = false, defaultValue="0") Optional<Object> id
    );


    @Operation(summary = "发送文件消息", description = "发送文件  Args:     path (str): 本地文件路径，如：`C:/Projs/WeChatRobot/README.MD`     receiver (str): 消息接收人，wxid 或者 roomid  Returns:     int: 0 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/file",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> sendFileFilePost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "发送图片消息", description = "发送图片，非线程安全  Args:     path (str): 图片路径，如：`C:/Projs/WeChatRobot/TEQuant.jpeg` 或 `https://raw.githubusercontent.com/lich0821/WeChatRobot/master/TEQuant.jpeg`     receiver (str): 消息接收人，wxid 或者 roomid  Returns:     int: 0 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/image",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> sendImageImagePost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "发送拍一拍消息", description = "拍一拍群友  Args:     roomid (str): 群友所在群 roomid     wxid (str): 要拍的群友的 wxid  Returns:     int: 1 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/pat",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> sendPatMsgPatPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody BodySendPatMsgPatPostRequest body
    );


    @Operation(summary = "发送卡片消息", description = "发送卡片消息 卡片样式（格式乱了，看这里：https://github.com/lich0821/WeChatFerry/blob/master/clients/python/wcferry/client.py#L421）：     |-------------------------------------|     |title, 最长两行     |(长标题, 标题短的话这行没有)     |digest, 最多三行，会占位    |--------|     |digest, 最多三行，会占位    |thumburl|     |digest, 最多三行，会占位    |--------|     |(account logo) name     |-------------------------------------| Args:     name (str): 左下显示的名字     account (str): 填公众号 id 可以显示对应的头像（gh_ 开头的）     title (str): 标题，最多两行     digest (str): 摘要，三行     url (str): 点击后跳转的链接     thumburl (str): 缩略图的链接     receiver (str): 接收人, wxid 或者 roomid  Returns:     int: 0 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/rich-text",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> sendRichTextRichTextPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body
    );


    @Operation(summary = "发送文本消息", description = "发送文本消息，可参考：https://github.com/lich0821/WeChatRobot/blob/master/robot.py 里 sendTextMsg  Args:     msg (str): 要发送的消息，换行使用 `\\n`；如果 @ 人的话，需要带上跟 `aters` 里数量相同的 @     receiver (str): 消息接收人，wxid 或者 roomid     aters (str): 要 @ 的 wxid，多个用逗号分隔；`@所有人` 只需要 `notify@all`  Returns:     int: 0 为成功，其他失败", tags={  })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "422", description = "Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HTTPValidationErrorResponse.class))) })
    @RequestMapping(value = "/text",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Object> sendTextTextPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody BodySendTextTextPostRequest body
    );

}
