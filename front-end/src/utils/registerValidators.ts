import { ref } from "vue";

//用户注册数据接口
interface RegisterUser {
  name: string;
  phone: string;
  password: string;
  password2: string;
  role: any;
}

export const registerUser = ref<RegisterUser>({
  name: "",
  phone: "",
  password: "",
  password2: "",
  role: null,
});

//注册规则接口
interface RegisterRules {
  name: ({
    message: string;
    required: boolean;
    trigger: string;
  } | {
    min: number;
    max: number;
    message: string;
    trigger: string;
  })[];
  password: ({
    required: boolean;
    message: string;
    trigger: string;
  } | {
    min: number;
    max: number;
    message: string;
    trigger: string;
  })[];
  password2: ({
    required: boolean;
    message: string;
    trigger: string;

  } | {
    min: number;
    max: number;
    message: string;
    trigger: string;

  } | {
    validator: (rule: RegisterRules, value: string, callback: any) => void;
    trigger: string;
  })[];
}

const validatePass2 = (rule: RegisterRules, value: string, callback: any) => {
  if (value === "") {
    callback(new Error("请再次输入密码"));
  } else if (value !== registerUser.value.password) {
    callback(new Error("两次输入密码不一致!"));
  } else {
    callback();
  }
};

export const registerRules = ref<RegisterRules>({
  name: [
    {
      message: "用户名不能为空...",
      required: true,
      trigger: "blur",
    },
    {
      min: 2,
      max: 30,
      message: "长度在2到30个字符",
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: "Password could not be empty...",
      trigger: "blur",
    },
    {
      min: 6,
      max: 30,
      message: "Password's length has to be 6 to 30 characters...",
      trigger: "blur",
    },
  ],
  password2: [
    {
      required: true,
      message: "Password2 could not be empty...",
      trigger: "blur",
    },
    {
      min: 6,
      max: 30,
      message: "Password's length has to be 6 to 30 characters...",
      trigger: "blur",
    },
    { validator: validatePass2, trigger: "blur" },
  ]

});