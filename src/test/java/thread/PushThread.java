package thread;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;

import entity.AcctBalance;
import repository.AcctBalanceRepository;

public class PushThread extends Thread{
	
	private static final Logger log = LoggerFactory.getLogger(PushThread.class);
	
	private int currentPage;
	private int pageSize;
	private int endPage;
	private RedisTemplate<String ,Object> template;
	private AcctBalanceRepository acctBalanceRepository;
	
	
	
	public PushThread(int currentPage, int pageSize, int endPage, RedisTemplate<String ,Object> template,
			AcctBalanceRepository acctBalanceRepository) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.endPage = endPage;
		this.template = template;
		this.acctBalanceRepository = acctBalanceRepository;
		this.setDaemon(true);
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
		for(int i = currentPage; i < endPage; i++) {
			log.info("{} - get {} page from db", Thread.currentThread().getName(), i);
			Page<AcctBalance> pageRes = acctBalanceRepository.findAll(new PageRequest(i, pageSize));
			template.opsForList().leftPushAll("allAcctBalance", (Collection)pageRes.getContent());
			log.info("{} - log {} page to redis complete!", Thread.currentThread().getName(), i);
		}
	}
	
	
	
	

}
