<template>
  <div class="customer-footer">
    <div class="container">
      <el-row :gutter="40">
        <el-col :span="6">
          <h4>About Us</h4>
          <ul>
            <li>Company</li>
            <li>Contact Us</li>
            <li>Careers</li>
          </ul>
        </el-col>
        <el-col :span="6">
          <h4>Support</h4>
          <ul>
            <li>Shipping</li>
            <li>Returns</li>
            <li>FAQ</li>
          </ul>
        </el-col>
        <el-col :span="6">
          <h4>Merchants</h4>
          <ul>
            <li><router-link to="/merchant">Join as a Merchant</router-link></li>
            <li>Merchant Login</li>
            <li>Merchant Help</li>
          </ul>
        </el-col>
        <el-col :span="6">
          <h4>Contact</h4>
          <ul>
            <li>Customer Service: 400-xxx-xxxx</li>
            <li>Email: service@flower.com</li>
            <li>Hours: 9:00-18:00</li>
          </ul>
        </el-col>
      </el-row>
      
      <div class="footer-bottom">
        <p>&copy; 2024 Flower Market. All Rights Reserved</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.customer-footer {
  background: #2c3e50;
  color: #ecf0f1;
  padding: var(--spacing-xl) 0 var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

.customer-footer h4 {
  font-size: var(--font-size-lg);
  margin-bottom: var(--spacing-md);
}

.customer-footer ul {
  list-style: none;
}

.customer-footer li {
  margin-bottom: var(--spacing-sm);
  cursor: pointer;
}

.customer-footer li:hover {
  color: var(--color-primary);
}

.footer-bottom {
  text-align: center;
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-md);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}
</style>
