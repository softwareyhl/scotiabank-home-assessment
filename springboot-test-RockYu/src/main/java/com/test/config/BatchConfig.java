package com.test.config;

import com.test.entity.Account;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job interestCalculationJob() {
        return jobBuilderFactory.get("interestCalculationJob")
                .incrementer(new RunIdIncrementer())
                .flow(interestCalculationStep())
                .end()
                .build();
    }

    @Bean
    public Step interestCalculationStep() {
        return stepBuilderFactory.get("interestCalculationStep")
                .<Account, Account>chunk(10)
                .reader(accountItemReader())
                .processor(accountItemProcessor())
                .writer(accountItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Account> accountItemReader() {
        JdbcCursorItemReader<Account> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM account");
        reader.setRowMapper(new BeanPropertyRowMapper<>(Account.class));
        return reader;
    }

    @Bean
    public ItemProcessor<Account, Account> accountItemProcessor() {

        return account -> {
            // Calculate interest and update balance
            double interest = account.getBalance() * 0.03;
            account.setBalance(account.getBalance() + interest);
            return account;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Account> accountItemWriter() {
        JdbcBatchItemWriter<Account> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("UPDATE account SET balance = :balance WHERE id = :id");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }
}