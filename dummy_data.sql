CREATE DATABASE ecommercev5;
USE ecommercev5;
SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0; -- Disable foreign key checks

-- Insert dummy users
-- Plain text passwords are stored in comments for development reference only
-- In production, never store or log plain text passwords
INSERT INTO users (email, password, role, name, address, phone_number, is_verified) VALUES
-- Password: customerpass -> Bruce Wayne
('bruce.wayne@wayne.enterprises', '$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y', 'USER', 'Bruce Wayne', '1007 Mountain Drive, Gotham City', '+1555BATMAN0', true),
-- Password: customerpass -> Clark Kent
('clark.kent@dailyplanet.com', '$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y', 'USER', 'Clark Kent', 'Kent Farm, Smallville', '+1555SUPERMN', true),

-- ADMIN -> Tony Stark
-- Password: adminpass
('tony.stark@starkindustries.com', '$2a$12$SATxIE9c94XvyC9hT7nHPOlyURChKKiqYPfV.G8gBesbQnYcgz/1.', 'ADMIN', 'Tony Stark', '1 Stark Tower, New York', '+1555IRONMAN', true),

-- STAFF
-- Password: staffpass -> Steve Rogers
('steve.rogers@avengers.com', '$2a$12$UC2VFiYEZWkJUE8xdgKiQO5KuWB62X4kNIoPti8K63i5Pz3B5T5fi', 'STAFF', 'Steve Rogers', 'Avengers Compound, Upstate NY', '+1555CAPTAIN', true),
-- Password: staffpass -> Natasha Romanoff
('natasha.romanoff@shield.gov', '$2a$12$UC2VFiYEZWkJUE8xdgKiQO5KuWB62X4kNIoPti8K63i5Pz3B5T5fi', 'STAFF', 'Natasha Romanoff', 'SHIELD HQ, Classified', '+1555WIDOW00', true),

-- NEW USERS
-- Password: customerpass -> Homelander
('homelander@vought.com', '$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y', 'USER', 'Homelander', 'Vought Tower, New York', '+1555VOUGHT1', true),
-- Password: customerpass -> Billy Butcher
('billy.butcher@theboys.com', '$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y', 'USER', 'Billy Butcher', 'Flatiron Building, New York', '+1555BUTCHER', false), -- Unverified user
-- Password: customerpass -> The Deep
('the.deep@vought.com', '$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y', 'USER', 'The Deep', 'Vought Tower, New York', '+1555DEEPSEA', true);

-- Insert staff entries
INSERT INTO staff (user_id, created_by) VALUES
(4, 3), -- Steve Rogers created by Tony Stark
(5, 3); -- Natasha Romanoff created by Tony Stark

-- Insert staff permissions
-- Staff One - Can view and update products, view orders
INSERT INTO staff_permissions (staff_id, permission) VALUES
(1, 'VIEW_PRODUCTS'),
(1, 'UPDATE_PRODUCTS'),
(1, 'ADD_PRODUCTS'),
(1, 'VIEW_ORDERS'),
(1, 'MANAGE_STOCK'),
(1, 'VIEW_STOCK_REPORTS');

-- Staff Two - Can view orders, update order status, view customers
INSERT INTO staff_permissions (staff_id, permission) VALUES
(2, 'VIEW_ORDERS'),
(2, 'UPDATE_ORDER_STATUS'),
(2, 'VIEW_CUSTOMERS'),
(2, 'MANAGE_STOCK'); -- Added stock management permission

-- Insert dummy products
INSERT INTO products (name, description, price, category, image_url, average_rating, review_count, stock) VALUES
('Slim Fit Blazer', 'Tailored for modern sophistication—ideal for formal evenings and sharp statements.', 129.99, 'Men', 'https://images.unsplash.com/photo-1630173250799-2813d34ed14b?q=80&w=1965&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 12, 25),
('Casual Denim Jacket', 'Relaxed silhouette with timeless denim texture—effortlessly cool.', 89.99, 'Men', 'https://plus.unsplash.com/premium_photo-1708274146193-1064e177ae39?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.7, 18, 32),
('Cotton Polo Shirt', 'Crisp, breathable cotton with an elevated casual appeal.', 29.99, 'Men', 'https://images.unsplash.com/photo-1625910513520-bed0389ce32f?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.3, 10, 50),
('Chinos Pants', 'Smart tailoring meets everyday ease in this modern classic.', 49.99, 'Men', 'https://images.unsplash.com/photo-1584865288642-42078afe6942?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.6, 14, 40),
('Graphic T-Shirt', 'Statement prints meet effortless minimalism.', 19.99, 'Men', 'https://images.unsplash.com/photo-1627225793904-a2f900a6e4cf?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.2, 25, 75),
('Leather Jacket', 'Supple leather cut for bold simplicity.', 199.99, 'Men', 'https://images.unsplash.com/photo-1521223890158-f9f7c3d5d504?q=80&w=1492&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.8, 30, 15),
('Formal Shirt', 'Clean lines and refined details for elevated daily wear.', 39.99, 'Men', 'https://plus.unsplash.com/premium_photo-1674777843130-40233fe45804?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 22, 45),
('Skinny Jeans', 'Streamlined silhouette with subtle stretch for modern comfort.', 59.99, 'Men', 'https://images.unsplash.com/photo-1475178626620-a4d074967452?q=80&w=1972&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.4, 16, 30),
('Wool Sweater', 'Soft, structured, and made for understated warmth.', 69.99, 'Men', 'https://plus.unsplash.com/premium_photo-1671135590215-ded219822a44?q=80&w=1973&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.7, 11, 22),
('Sports Hoodie', 'Sleek functionality with a minimalist edge.', 49.99, 'Men', 'https://images.unsplash.com/photo-1542406775-ade58c52d2e4?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.3, 15, 27),
('Floral Maxi Dress', 'Soft drape and floral expression—crafted for elegant movement.', 89.99, 'Women', 'https://images.unsplash.com/photo-1692220438343-d054c8b7d7c0?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.6, 24, 18),
('Pleated Midi Skirt', 'A modern flow in structured pleats—effortlessly refined.', 49.99, 'Women', 'https://images.unsplash.com/photo-1631978278971-9afda1670882?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 17, 24),
('High-Waist Jeans', 'Flattering elevation meets clean denim lines.', 59.99, 'Women', 'https://images.unsplash.com/photo-1678219376035-90da4bda4926?q=80&w=1946&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.7, 20, 35),
('Wrap Dress', 'Fluid form with a defined waist—timeless and versatile.', 69.99, 'Women', 'https://images.unsplash.com/photo-1508829298730-713792c22189?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 25, 20),
('Oversized Coat', 'Sculptural volume in a clean, winter-ready form.', 149.00, 'Women', 'https://images.unsplash.com/photo-1739620620741-f7b23ede3149?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.6, 19, 12),
('Classic Leather Belt', 'Polished simplicity—crafted in genuine leather.', 29.99, 'Accessories', 'https://images.unsplash.com/photo-1664285612706-b32633c95820?q=80&w=2158&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.4, 11, 50),
('Casual Sneakers', 'Minimalist design meets all-day comfort.', 59.99, 'Men', 'https://images.unsplash.com/photo-1736555142217-916540c7f1b7?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.3, 23, 40),
('Elegant Silk Tie', 'Silk refinement for an elevated occasion.', 19.99, 'Accessories', 'https://plus.unsplash.com/premium_photo-1668618919861-9e85e97657e0?q=80&w=2072&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.7, 15, 60),
('Vintage Sunglasses', 'Retro geometry meets modern UV clarity.', 39.99, 'Accessories', 'https://plus.unsplash.com/premium_photo-1692340973681-e96b10bda346?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 28, 35),
('Designer Scarf', 'Textural softness wrapped in minimal luxury.', 34.99, 'Accessories', 'https://images.unsplash.com/photo-1738774106908-04ea849dec8e?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.6, 18, 45),
('Ankle Boots', 'Sleek contours crafted for seasonal transitions.', 89.99, 'Women', 'https://images.unsplash.com/photo-1574413230119-f302e1c9035d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.8, 21, 30),
('Lace-up Oxford Shoes', 'Classic meets contemporary in smooth, sculpted leather.', 109.99, 'Men', 'https://plus.unsplash.com/premium_photo-1674770380314-d1639a2d4b77?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 19, 25),
('Boho Maxi Skirt', 'Bohemian lines in relaxed, airy movement.', 44.99, 'Women', 'https://images.unsplash.com/photo-1660997351262-6c31d8a35b6c?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.4, 12, 40),
('Cargo Shorts', 'Functional and fresh—designed for sunlit adventures.', 34.99, 'Men', 'https://images.unsplash.com/photo-1695918428860-a3bc6a2f1d4b?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.3, 11, 40),
('Fitted Tank Top', 'Streamlined and airy with subtle structure.', 24.99, 'Women', 'https://images.unsplash.com/photo-1571666274590-f8cc87006500?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.2, 14, 70),
('Summer Hat', 'Refined sun protection with vintage-inspired charm.', 19.99, 'Accessories', 'https://images.unsplash.com/photo-1719664026208-34ad5c25d0a9?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.6, 10, 50),
('Denim Overalls', 'Effortless layering piece in contemporary denim.', 59.99, 'Women', 'https://plus.unsplash.com/premium_photo-1661286730661-3f12452bb601?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 16, 42),
('Puffer Vest', 'Light structure with a clean finish—layering made simple.', 69.99, 'Men', 'https://plus.unsplash.com/premium_photo-1708274927606-0f186d55a7aa?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.4, 19, 33),
('Button-Down Casual Shirt', 'Crisp construction designed for fluid transition.', 44.99, 'Men', 'https://images.unsplash.com/photo-1713816302861-1ff8d4b09fad?q=80&w=2026&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.3, 21, 38),
('Athletic Leggings', 'Contoured movement in sleek activewear.', 39.99, 'Women', 'https://images.unsplash.com/photo-1600348077475-d4db860d06f7?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.5, 26, 55),
('Running Shoes', 'Lightweight performance for optimal speed and comfort.', 79.99, 'Men', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.7, 35, 60),
('Yoga Mat', 'Non-slip surface for stability and support during practice.', 24.99, 'Accessories', 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=2120&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 4.9, 40, 80);

-- Insert dummy reviews
INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES
(1, 1, 5, 'Great blazer! Perfect fit and excellent quality.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY)),
(1, 2, 4, 'Good blazer but slightly longer arms than expected', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY)),
(1, 6, 5, 'Very stylish, fits perfectly.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY)),
(2, 1, 5, 'This denim jacket is amazing - goes with everything', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY)),
(2, 8, 4, 'Comfortable and looks good, but the buttons feel a bit loose.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY)),
(3, 2, 5, 'The polo is very comfortable and breathable', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY)),
(3, 6, 4, 'Nice color, good fit.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY)),
(4, 1, 4, 'Nice chinos, comfortable for office wear', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY)),
(4, 8, 5, 'Best chinos I have owned. Great material.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(5, 2, 3, 'The print faded after a few washes.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY)),
(6, 1, 5, 'Expensive but worth it. Fantastic quality leather.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY)),
(7, 6, 4, 'Good formal shirt for the price.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY)),
(11, 2, 5, 'Beautiful dress, flows nicely.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY)),
(12, 8, 3, 'Skirt material is thinner than I expected.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY)),
(17, 1, 5, 'Very comfy sneakers for walking.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY)),
(21, 2, 4, 'Good boots, keep my feet warm.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));


-- Insert dummy cart items
TRUNCATE TABLE cart_items;
INSERT INTO cart_items (user_id, product_id, quantity, price) VALUES
(1, 6, 1, 199.99), -- Leather Jacket
(1, 17, 1, 59.99), -- Casual Sneakers
(2, 11, 1, 89.99), -- Floral Maxi Dress
(2, 20, 2, 34.99), -- Designer Scarf (x2)
(6, 3, 1, 29.99), -- Cotton Polo Shirt
(8, 13, 1, 59.99); -- High-Waist Jeans


-- Insert dummy orders
TRUNCATE TABLE orders;
INSERT INTO orders (user_id, total_amount, shipping_address, status, payment_status) VALUES
(1, 189.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID'),       -- Order ID 1 (Bruce Wayne - Blazer + 2x Polo = 129.99 + 2*29.99 = 189.97)
(2, 139.98, 'Kent Farm, Smallville', 'SHIPPED', 'PAID'),             -- Order ID 2 (Clark Kent - Denim Jacket + Chinos = 89.99 + 49.99 = 139.98)
(1, 49.99, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID'),       -- Order ID 3 (Bruce Wayne - Chinos = 49.99)
(6, 119.98, 'Vought Tower, New York', 'CONFIRMED', 'PAID'),           -- Order ID 4 (Homelander - Sweater + Hoodie = 69.99 + 49.99 = 119.98)
(7, 84.98, 'Flatiron Building, New York', 'PENDING', 'PENDING'),      -- Order ID 5 (Billy Butcher - Jeans + Tank = 59.99 + 24.99 = 84.98)
(2, 19.99, 'Kent Farm, Smallville', 'CANCELLED', 'REFUNDED'),          -- Order ID 6 (Clark Kent - Hat = 19.99)
-- Adding more orders
(1, 259.98, '1007 Mountain Drive, Gotham City', 'SHIPPED', 'PAID'),       -- Order ID 7 (Bruce: Leather Jacket + Sneakers = 199.99 + 59.99 = 259.98)
(4, 59.98, 'Avengers Compound, Upstate NY', 'DELIVERED', 'PAID'),      -- Order ID 8 (Steve: Formal Shirt + Tie = 39.99 + 19.99 = 59.98)
(5, 149.00, 'SHIELD HQ, Classified', 'DELIVERED', 'PAID'),           -- Order ID 9 (Natasha: Oversized Coat = 149.00)
(8, 124.98, 'Vought Tower, New York', 'PENDING', 'PENDING'),          -- Order ID 10 (Deep: Ankle Boots + Scarf = 89.99 + 34.99 = 124.98)
(6, 139.98, 'Vought Tower, New York', 'SHIPPED', 'PAID'),           -- Order ID 11 (Homelander: Oxford Shoes + Belt = 109.99 + 29.99 = 139.98)
(7, 79.99, 'Flatiron Building, New York', 'CONFIRMED', 'PAID'),     -- Order ID 12 (Butcher: Running Shoes = 79.99)
(1, 179.98, 'Wayne Manor, Gotham Outskirts', 'CONFIRMED', 'PAID'),   -- Order ID 13 (Bruce: Blazer + Chinos = 129.99 + 49.99 = 179.98)
(2, 89.99, 'Daily Planet, Metropolis', 'DELIVERED', 'PAID'),         -- Order ID 14 (Clark: Denim Jacket = 89.99)
(5, 64.98, 'SHIELD HQ, Classified', 'SHIPPED', 'PAID'),           -- Order ID 15 (Natasha: Athletic Leggings + Tank Top = 39.99 + 24.99 = 64.98)
(8, 44.99, 'Vought Tower, New York', 'DELIVERED', 'PAID'),          -- Order ID 16 (Deep: Boho Skirt = 44.99)
(4, 59.98, 'Avengers Compound, Upstate NY', 'CONFIRMED', 'PAID'),      -- Order ID 17 (Steve: Polo (x2) = 2*29.99 = 59.98)
(6, 119.98, 'Vought Tower, New York', 'PENDING', 'PENDING'),         -- Order ID 18 (Homelander: Puffer Vest + Hoodie = 69.99 + 49.99 = 119.98)
(7, 74.97, 'Flatiron Building, New York', 'DELIVERED', 'PAID'),      -- Order ID 19 (Butcher: Cargo Shorts + T-Shirt + Hat = 34.99 + 19.99 + 19.99 = 74.97)
(1, 24.99, '1007 Mountain Drive, Gotham City', 'CANCELLED', 'REFUNDED'),  -- Order ID 20 (Bruce: Yoga Mat = 24.99)
(2, 129.98, 'Kent Farm, Smallville', 'SHIPPED', 'PAID');            -- Order ID 21 (Clark: Wrap Dress + Sneakers = 69.99 + 59.99 = 129.98)

-- Insert dummy order items
TRUNCATE TABLE order_items;
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
-- Existing Order Items
(1, 1, 1, 129.99), -- Blazer
(1, 3, 2, 29.99),  -- Polo (x2)
(2, 2, 1, 89.99),  -- Denim Jacket
(2, 4, 1, 49.99),  -- Chinos
(3, 4, 1, 49.99),  -- Chinos
(4, 9, 1, 69.99),  -- Wool Sweater
(4, 10, 1, 49.99), -- Sports Hoodie
(5, 13, 1, 59.99), -- High-Waist Jeans
(5, 25, 1, 24.99), -- Fitted Tank Top
(6, 26, 1, 19.99), -- Summer Hat (Order cancelled)
-- Added Order Items
(7, 6, 1, 199.99),  -- Leather Jacket
(7, 17, 1, 59.99),  -- Casual Sneakers
(8, 7, 1, 39.99),   -- Formal Shirt
(8, 18, 1, 19.99),  -- Elegant Silk Tie
(9, 15, 1, 149.00), -- Oversized Coat
(10, 21, 1, 89.99), -- Ankle Boots
(10, 20, 1, 34.99), -- Designer Scarf
(11, 22, 1, 109.99), -- Lace-up Oxford Shoes
(11, 16, 1, 29.99), -- Classic Leather Belt
(12, 31, 1, 79.99), -- Running Shoes
(13, 1, 1, 129.99), -- Slim Fit Blazer
(13, 4, 1, 49.99),  -- Chinos Pants
(14, 2, 1, 89.99),  -- Casual Denim Jacket
(15, 30, 1, 39.99), -- Athletic Leggings
(15, 25, 1, 24.99), -- Fitted Tank Top
(16, 23, 1, 44.99), -- Boho Maxi Skirt
(17, 3, 2, 29.99),  -- Cotton Polo Shirt (x2)
(18, 28, 1, 69.99), -- Puffer Vest
(18, 10, 1, 49.99), -- Sports Hoodie
(19, 24, 1, 34.99), -- Cargo Shorts
(19, 5, 1, 19.99),  -- Graphic T-Shirt
(19, 26, 1, 19.99), -- Summer Hat
(20, 32, 1, 24.99), -- Yoga Mat (Order cancelled)
(21, 14, 1, 69.99), -- Wrap Dress
(21, 17, 1, 59.99); -- Casual Sneakers

SET SQL_SAFE_UPDATES = 1; -- Re-enable safe updates
SET FOREIGN_KEY_CHECKS = 1; -- Re-enable foreign key checks

-- Update orders table with correct total amounts calculated from order_items
UPDATE orders o 
SET total_amount = (
    SELECT SUM(oi.price * oi.quantity) 
    FROM order_items oi 
    WHERE oi.order_id = o.id
);

-- End of script
