package vendingmachine.domain;

import java.util.List;
import java.util.Map;

public class VendingMachine {

    private static final String ERROR_HEADER = "[ERROR] ";
    private static final String NOT_EXIST_PRODUCT = "구매할 수 있는 상품이 존재하지 않습니다. 자판기에서 구매할 수 있고 존재하는 상품을 입력해주세요. ";

    private final CoinCountMap coinCountMap;
    private final List<Product> products;
    private int inputMoney;

    public VendingMachine(CoinCountMap coinCountMap, List<Product> products, int inputMoney) {
        this.coinCountMap = coinCountMap;
        this.products = products;
        this.inputMoney = inputMoney;
    }

    public void buyProduct(String inputProductName) {
        Product product = findProudctInVendingMachine(inputProductName);
        inputMoney -= product.getPrice();
        product.buyOneProduct();

    }

    public CoinCountMap getLeftoverCash() {
        if (inputMoney >= coinCountMap.getCoinSum()) {
            return coinCountMap;
        }
        return calculateLeftoverCash(inputMoney);
    }

    public int getInputMoney() {
        return inputMoney;
    }

    public int getProductsCount() {
        int counts = 0;
        for (Product product : products) {
            counts += product.getCount();
        }
        return counts;
    }

    public int getMinPrice() {
        int minValue = Integer.MAX_VALUE;
        for (Product product : products) {
            if (product.getPrice() < minValue) {
                minValue = product.getPrice();
            }
        }
        return minValue;
    }


    private Product findProudctInVendingMachine(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName) && inputMoney >= product.getPrice() && product.getCount() > 0) {
                return product;
            }
        }
        throw new IllegalArgumentException(ERROR_HEADER + NOT_EXIST_PRODUCT);
    }

    private CoinCountMap calculateLeftoverCash(int toReturnCash ) {
        CoinCountMap leftoverCoinCountMap = new CoinCountMap();
        Coin[] coinArray = Coin.values();
        int idx = 0;
        while (toReturnCash != 0) {
            Coin coin = coinArray[idx];
            if (this.coinCountMap.getCoinCount().get(coin) == 0) {
                idx += 1;
                continue;
            }
            int minCoinCount = findMinCoinCount(idx, coinArray, toReturnCash);
            toReturnCash -= minCoinCount * coin.getAmount();
            leftoverCoinCountMap.getCoinCount().replace(coin, leftoverCoinCountMap.getCoinCount().get(coin) + minCoinCount);
            this.coinCountMap.getCoinCount().replace(coin, this.coinCountMap.getCoinCount().get(coin) - minCoinCount);
        }
        return leftoverCoinCountMap;
    }

    private int findMinCoinCount(int idx, Coin[] coinArray, int toReturnCash) {
        int coinValue = coinArray[idx].getAmount();
        return Math.min(toReturnCash / coinValue, this.coinCountMap.getCoinCount().get(coinArray[idx]));

    }


}
