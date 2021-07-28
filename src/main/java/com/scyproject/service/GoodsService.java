package com.scyproject.service;

import java.util.List;

import com.scyproject.dao.GoodsDao;
import com.scyproject.domain.Goods;
import com.scyproject.domain.MiaoshaGoods;
import com.scyproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class GoodsService {
	
	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoById(long id) {
		return goodsDao.getGoodsVoById(id);
	}

	public void reduceStock(Goods goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		goodsDao.reduceStock(g);
	}


}
