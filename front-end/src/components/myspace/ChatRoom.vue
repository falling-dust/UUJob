<template>
  <div class="chatroom">
    <div class="messages">
      <el-scrollbar ref="messagesScroll" style="width: 100%;">
        <div class="mb-5" v-for="message in messages" :key="message.id"
          :class="{ 'my-message': message.isMine, 'their-message': !message.isMine }">
          <img class="avatar" v-if="!message.isMine" :src="message.avatar" alt="user" />
          <div class="message-main">
            <div class="message-content" :class="{ 'my-bubble': message.isMine, 'their-bubble': !message.isMine }">
              <div v-if="message.type === 'text'">
                {{ message.content }}
              </div>
              <div v-if="message.type === 'image'">
                <img :src="message.content" class="uploaded-image" @click="downloadFile(message.content)" />
              </div>
              <div v-if="message.type === 'file'">
                <a :href="message.content" download @click="downloadFile(message.content)">
                  {{ message.fileName }}
                </a>
              </div>
            </div>
            <div class="message-time">
              {{ message.createdAt }}
            </div>
          </div>
          <img class="avatar" v-if="message.isMine" :src="message.avatar" alt="user" />
        </div>

      </el-scrollbar>
    </div>

    <div class="input-area">
      <div class="button-area mb-1">
        <div>
          <input type="file" ref="fileInput" class="file-input" @change="uploadFile" />
          <el-button size="large" @click="openFilePicker" circle><el-icon size="large">
              <DocumentAdd />
            </el-icon></el-button>
          <el-button size="large" @click="openFilePicker" circle><el-icon size="large">
              <Picture />
            </el-icon></el-button>

          <el-button size="large" @click="scrollToBottom" circle><el-icon size="large">
              <ArrowDown />
            </el-icon></el-button>

          <el-button size="large" @click="scrollToTop" circle><el-icon size="large">
              <ArrowUp />
            </el-icon></el-button>
        </div>
        <el-button style="width: 100px;" color="#009080" @click="sendMessage"><el-icon>
            <Position />
          </el-icon>发送</el-button>
      </div>
      <textarea style="overflow-y: auto;" v-model="newMessage" placeholder="输入消息..."
        @keydown.enter="sendMessage"></textarea>
    </div>
  </div>
</template>

<script>
import { useRoute } from 'vue-router';
import { useStore } from 'vuex';
import axios from "axios";

export default {
  data() {
    return {
      contactId: useRoute().params.contactId,
      userId: useStore().state.userId,
      conversationId: useRoute().params.conversationId,
      messages: [
        // 示例聊天记录
        {
          id: '111',
          content: 'Hi, how are you?Hi, how are you?Hi, how are you?Hi, how are you?Hi, how are you?Hi, how are you?',
          type: 'text',
          isMine: false,
          avatar: 'https://via.placeholder.com/40',
        },
        {
          id: '111',
          content: 'Hi, how are you?Hi, how are you?Hi, how are you?Hi, how are you?Hi, how are you?Hi, how are you?',
          type: 'text',
          isMine: false,
          avatar: 'https://via.placeholder.com/40',
        },
        {
          id: '112',
          content: 'I\'m good, thanks!',
          type: 'text',
          isMine: true,
          avatar: 'https://via.placeholder.com/40',
        },
        {
          id: '112',
          content: 'I\'m good, thanks!',
          type: 'text',
          isMine: true,
          avatar: 'https://via.placeholder.com/40',
        },
      ],
      newMessage: '',
      fileInput: null,
    };
  },
  methods: {
    async getHistoryMessages() {
      try {
        // Replace the URL with your API endpoint to fetch chats
        const response = await axios.get("http://localhost:9090/conversations/messages", {
          params: {
            conversationId: this.conversationId,
          },
        });
        if (response.data.code === 200) {
          console.log('##################')
          console.log(response.data.data);
          const messageList = response.data.data;
          messageList.forEach(message => {
            if (message.senderId == this.userId) {
              this.contactId = message.receiverId
              message.isMine = true
            } else {
              message.isMine = false
            }
            message.type = 'text';
            var date = new Date(message.createdAt);
            message.createdAt = (date.getMonth() + 1).toString().padStart(2, '0') + "-" + date.getDate().toString().padStart(2, '0')
              + " " + date.getHours().toString() + ":" + date.getMinutes().toString().padStart(2, '0');
            //头像有待处理
            message.avatar = 'https://via.placeholder.com/40'
          });


          this.messages = messageList;

          //this.scrollToBottom()
        } else {
          alert(response.data.msg);
        }
      } catch (error) {
        console.error("Function getHistoryMessages() failed to fetch chat list:", error);
      }
    },
    async sendMessage() {
      try {
        // Replace the URL with your API endpoint to fetch chats
        const response = await axios.post("http://localhost:9090/conversations/messages", {
          senderId: this.userId,
          receiverId: this.contactId,
          content: this.newMessage,
          conversationId: this.conversationId
        });
        if (response.data.code === 200) {
          this.$message.success('发送成功')


          this.newMessage = ''
          await this.getHistoryMessages();
          this.scrollToBottom()


          this.$emit("message-sent", {
            chatId: this.conversationId,
            lastMessage: this.newMessage,
            lastMessageTime: new Date().toISOString(),
          });
        } else {
          alert(response.data.msg);
        }
      } catch (error) {
        console.error("Failed to fetch chat list:", error);
      }
    },
    openFilePicker() {
      this.$refs.fileInput.click();
    },
    uploadFile(event) {
      // TODO: 上传文件到服务器
      // 模拟上传成功后，将文件/图片添加到聊天记录
      this.messages.push({
        id: Date.now(),
        type: 'file',
        content: URL.createObjectURL(event.target.files[0]),
        fileName: event.target.files[0].name,
        isMine: true,
      });
      event.target.value = null;
    },
    downloadFile(url) {
      // TODO: 下载文件
    },
    startPolling() {
      // 每两秒钟调用一次getMessages方法
      this.pollingIntervalId = setInterval(() => {
        //alert('转一下')
        this.getHistoryMessages();
      }, 2000);
    },
    stopPolling() {
      clearInterval(this.pollingIntervalId);
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const scrollContainer = this.$refs.messagesScroll;
        const innerContentHeight = scrollContainer.$el.querySelector(".el-scrollbar__wrap").scrollHeight;
        scrollContainer.scrollTo({ top: innerContentHeight });
      });
    },
    scrollToTop() {
      this.$nextTick(() => {
        const scrollContainer = this.$refs.messagesScroll;
        scrollContainer.scrollTo({ top: 0 });
      });
    },
    async initPage() {
      await this.getHistoryMessages();
      this.scrollToBottom();
    },
  },
  watch: {
    '$route.params.conversationId': {
      immediate: true,
      handler(conversationId) {
        this.conversationId = conversationId;
        this.getHistoryMessages();
      },
    },
    '$route.params.contactId': {
      immediate: true,
      handler(contactId) {
        this.contactId = contactId;
        //this.getHistoryMessages();
      },
    },
  },
  mounted() {
    this.initPage();
    // 在组件加载时开始轮询   
    // this,scrollToBottom();
    this.startPolling();
    //this.getHistoryMessages()
  },
  updated() {
    this.initPage();
  },
  unmounted() {
    // 在组件销毁时停止轮询
    this.stopPolling();
  },
};
</script>

<style scoped>
.chatroom {
  display: flex;
  flex-direction: column;
  height: 800px;
}

.messages {
  overflow: auto;
  height: 50%;
  padding: 1rem;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  position: relative;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-list-item {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 1rem;
}

.my-message .avatar {
  margin-left: 20px;
}

.my-message {
  display: flex;
  width: 100%;
  justify-content: flex-end;
  padding-right: 40px;
}

.their-message {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  position: relative;
}

.their-message .avatar {
  margin-right: 20px;
}

.their-message .message-content {
  border-radius: 5px;
  padding: 0.5rem;
  position: relative;
  max-width: 500px;
}

.my-message .message-content {
  border-radius: 5px;
  padding: 0.5rem;
  position: relative;
  max-width: 500px;
}

.my-message .message-time {
  float: right;
}

.their-message .message-time {
  float: left;
}

.my-bubble {
  background-color: teal;
  color: #fff;
  border: 1px solid teal;
  border-radius: 5px 5px 0 5px;
  margin-right: 0px;
}

.my-bubble::after {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  top: 13px;
  right: -10px;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-left: 10px solid teal;
}

.their-bubble {
  background-color: #fff;
  color: #333;
  border: 1px solid teal;
  border-radius: 5px 5px 5px 0;
}

.their-bubble::before {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  top: 13px;
  left: -15px;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-right: 15px solid teal;
}

.their-bubble::after {
  content: '';
  position: absolute;
  width: 0;
  height: 0;
  top: 13px;
  left: -13px;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-right: 15px solid #fff;
}

.uploaded-image {
  max-width: 200px;
  cursor: pointer;
}

.input-area {
  height: 50%;
  padding: 1rem;
}

.file-input {
  display: none;
}

.button-area {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

textarea {
  width: 100%;
  margin-right: 1rem;
  outline: none;
  height: 80%;
}
</style>