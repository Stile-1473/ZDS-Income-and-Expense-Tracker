package com.ZerodaySolution.Billing.services;


import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.entity.ExpenseEntity;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import com.ZerodaySolution.Billing.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ExpenseService expenseService;
    private final ProfileRepository profileService;
    private final EmailService emailService;

    @Value("${Billing.system.frontend.url}")
    private String frontendUrl;
//Cron jobs

  //  @Scheduled(cron = "0 * * * * *",zone = "Africa/Harare")
    @Scheduled(cron = "0 0 10 * * *",zone = "Africa/Harare")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");
         List<ProfileEntity> profiles = profileService.findAll();

         for (ProfileEntity profile : profiles){
             String body = "Hi" + profile.getFullName()+ "<br><br>" +
                     "This is a friendly reminder to not forget to add your daily incomes and expenses in your app <br><br>" +
                     "<br><br> <a href="+frontendUrl+"> Your IE App </a>" +
                     "<br><br>Best regards ZeroDaySolutions Team";

             emailService.sendMail(profile.getEmail(),"Daily Reminder",body);

         }
        log.info("Job completed: sendDailyIncomeExpenseReminder()");


    }
  // @Scheduled(cron = "0 * * * * *",zone = "Africa/Harare")
    @Scheduled(cron = "0 0 23 * * *",zone = "Africa/Harare")
    public void sendDailyExpenseSummary(){
        log.info("Job started:  sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileService.findAll();


        for (ProfileEntity profile : profiles){
            List<ExpenseDTO> exp =expenseService.getExpenseOnTheDate(profile.getId(), LocalDate.now(ZoneId.of("Africa/Harare")));
            if (!exp.isEmpty()){


                StringBuilder table = new StringBuilder();


                table.append("<table style='border-collapse:collapse;width:100%;font-family:Arial,sans-serif;font-size:14px;'>");
                table.append("<thead>");
                table.append("<tr style='background-color:#007bff;color:white;text-align:center;'>");
                table.append("<th style='padding:10px;border:1px solid #ddd;'>Name</th>");
                table.append("<th style='padding:10px;border:1px solid #ddd;'>Category</th>");
                table.append("<th style='padding:10px;border:1px solid #ddd;'>Amount</th>");
                table.append("<th style='padding:10px;border:1px solid #ddd;'>Date</th>");
                table.append("</tr>");
                table.append("</thead>");
                table.append("<tbody>");

                int i = 1;
                for (ExpenseDTO expense : exp) {
                    table.append("<tr style='text-align:center;font-family:Arial,sans-serif;font-size:13px;'>");

                    // Index / #
                    table.append("<td style='border:1px solid #ddd;padding:10px;background-color:#f9f9f9;color:#333;'>")
                            .append(i++)
                            .append("</td>");

                    // Name
                    table.append("<td style='border:1px solid #ddd;padding:10px;background-color:#ffffff;color:#333;'>")
                            .append(expense.getName())
                            .append("</td>");

                    // Category
                    table.append("<td style='border:1px solid #ddd;padding:10px;background-color:#ffffff;color:#007bff;font-weight:500;'>")
                            .append(expense.getId()!= null ? expense.getCategoryName() : "N / A") // if you have category name
                            .append("</td>");

                    // Amount
                    table.append("<td style='border:1px solid #ddd;padding:10px;background-color:#ffffff;color:#28a745;font-weight:bold;'>$")
                            .append(String.format("%.2f", expense.getAmount()))
                            .append("</td>");

                    // Date
                    table.append("<td style='border:1px solid #ddd;padding:10px;background-color:#ffffff;color:#666;'>")
                            .append(expense.getDate())
                            .append("</td>");

                    table.append("</tr>");
                }

// 🧩 Close table
                table.append("</tbody></table>");

                String body = "hi" + profile.getFullName() + "here is a summary of your expense for today :" + table + "<br/> <br/>"
                        + "Bast regards Zeroday Solutions Team";

                emailService.sendMail(profile.getEmail(),"Your Daily Expenses",body);
            }
        }

        log.info("Job completed:  sendDailyExpenseSummary()");

    }
}
