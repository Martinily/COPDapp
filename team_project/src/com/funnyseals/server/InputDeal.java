package com.funnyseals.server;

import java.io.IOException;

import org.json.JSONObject;

public interface InputDeal
{

    static void BasicDeal(String tempjson) throws IOException
    {
        JSONObject json=new JSONObject(tempjson);
        String type="0";
        type=json.getString("request_type");

        if(type.equals("1"))//登录
        {
            //System.out.println("00");
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String username=json.getString("user_name");
                        String password=json.getString("user_pw");
                        //System.out.println("0");
                        Login.login(username,password);
                    }
                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("2"))//注册
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //System.out.println(tempjson+"!");
                        //System.out.println("reg!");
                        //System.out.println(news+"!!");
                        String register_type=json.getString("register_type");
                        Register.register(tempjson,register_type);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("3"))    //发布护理计划
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        ReleasePlan.releasePlan(tempjson);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("4"))    //查看护理计划
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String user_type=json.getString("user_type");
                        QueryPlan.queryPlan(tempjson,user_type);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("5"))    //查看护理计划具体项目
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String planID=json.getString("planID");
                        QueryPlanItem.queryPlanItem(planID);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("6"))   //查看个人信息
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String user_type=json.getString("user_type");
                        String ID=json.getString("ID");
                        PersonalInfor.personalInfor(ID,user_type);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("7"))   //修改个人信息
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String user_type=json.getString("user_type");
                        ModifyInfo.modifyInfo(tempjson,user_type);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("8"))   //修改密码
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String Pmodify_type=json.getString("modify_type");
                        PasswordModify.passwordModify(tempjson,Pmodify_type);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("9"))   //查看/添加/修改 患者设备
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        ConfigureDevice.configureDevice(tempjson);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("11"))   //查看/添加 患者数据
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        PatientData.patientData(tempjson);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }
        else if(type.equals("12"))   //查看患者列表
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String docID=json.getString("docID");
                        PatientList.patientList(docID);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }
        else if(type.equals("13"))   //查看药物/器械库
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String base_type=json.getString("base_type");
                        QueryBase.queryBase(base_type);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("14"))   // 修改护理计划
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        UpdatePlan.updatePlan(tempjson);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }

        else if(type.equals("15"))   // 查看/添加  患者病情历史
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String pID=json.getString("pID");
                        String history_type=json.getString("history_type");
                        ConditionHistory.conditionHistory(pID,history_type);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }
    }

    public static void main(String[] args)
    {

    }
}

