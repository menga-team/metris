import threading
import time
from collections import Counter
from math import sqrt
from random import shuffle

import pygame
import random

from pygame import Color

# Initialize pygame
pygame.init()

# Constants
BLOCK_SIZE = 50
COLUMNS, ROWS = 10, 20
WIDTH, HEIGHT = BLOCK_SIZE*COLUMNS, BLOCK_SIZE*ROWS
(WHITE, BLACK, GRAY) = (255, 255, 255), (0, 0, 0), (128, 128, 128)
(RED, GREEN, BLUE, YELLOW, CYAN, ORANGE, PURPLE) = (255, 0, 0), (0, 255, 0), (0, 0, 255), (255, 255, 0), (0, 255, 255), (255, 128, 0), (255, 0, 255)

# Shapes
SHAPES = [
    [[
        [0, 0, 0, 0],
        [1, 1, 1, 1],
        [0, 0, 0, 0],
        [0, 0, 0, 0],
            ],[
        [0, 1, 0, 0],
        [0, 1, 0, 0],
        [0, 1, 0, 0],
        [0, 1, 0, 0],
      ]],  # I

    [[
        [1, 0, 0],
        [1, 1, 1],
        [0, 0, 0],
            ],[
        [0, 1, 1],
        [0, 1, 0],
        [0, 1, 0],
            ],[
        [0, 0, 0],
        [1, 1, 1],
        [0, 0, 1],
            ],[
        [0, 1, 0],
        [0, 1, 0],
        [1, 1, 0],
    ]],  # J
    [[
        [0, 0, 1],
        [1, 1, 1],
        [0, 0, 0],
            ],[
        [0, 1, 0],
        [0, 1, 0],
        [0, 1, 1],
            ],[
        [0, 0, 0],
        [1, 1, 1],
        [1, 0, 0],
            ],[
        [1, 1, 0],
        [0, 1, 0],
        [0, 1, 0],
    ]],  # L
    [[
        [1, 1],
        [1, 1]
    ]],  # O

    [[
        [0, 1, 1],
        [1, 1, 0],
        [0, 0, 0],
            ],[
        [0, 1, 0],
        [0, 1, 1],
        [0, 0, 1],
            ],[
        [0, 0, 0],
        [0, 1, 1],
        [1, 1, 0],
            ],[
        [1, 0, 0],
        [1, 1, 0],
        [0, 1, 0],
    ]],  # S

    [[
        [0, 1, 0],
        [1, 1, 1],
        [0, 0, 0],
    ],[
        [0, 1, 0],
        [0, 1, 1],
        [0, 1, 0],
    ],[
        [0, 0, 0],
        [1, 1, 1],
        [0, 1, 0],
    ],[
        [0, 1, 0],
        [1, 1, 0],
        [0, 1, 0],
    ]],  # T
    [[
        [1, 1, 0],
        [0, 1, 1],
        [0, 0, 0],
    ],[
        [0, 0, 1],
        [0, 1, 1],
        [0, 1, 0],
    ],[
        [0, 0, 0],
        [1, 1, 0],
        [0, 1, 1],
    ],[
        [0, 1, 0],
        [1, 1, 0],
        [1, 0, 0],
    ]],  # Z
]


class Tetrimino:
    def __init__(self, shape_index=None, x=None, y=0,rotation=0):
        if shape_index is None:
            self.shape_index = random.randint(0, len(SHAPES)-1)
        else:
            self.shape_index = shape_index
        self.shape = SHAPES[self.shape_index]
        # self.shape = SHAPES[3]
        self.rotate(rotation)
        self.tetromino = self.shape[self.rotation]
        if x is None:
            self.x = COLUMNS // 2 - len(self.tetromino[0]) // 2
        else:
            self.x = x
        self.y = y
        self.color = [BLUE, GREEN, RED, YELLOW, CYAN, ORANGE, PURPLE][self.shape_index]

    def copy(self, tetro):
        self.shape = tetro.shape
        self.shape_index = tetro.shape_index
        self.rotate(tetro.rotation)
        self.tetromino = tetro.tetromino
        self.x = tetro.x
        self.y = tetro.y
        self.color = tetro.color

    def modulo_rotation(self, rot):
        return rot % (len(self.shape))

    def rotate(self, rot):
        self.rotation = self.modulo_rotation(rot)
        self.tetromino = self.shape[self.rotation]


class Tetris:
    def __init__(self):
        self.grid = [[BLACK for _ in range(COLUMNS)] for _ in range(ROWS)]
        self.past_pieces = []
        self.current_piece = Tetrimino()
        self.running = True
        self.score = 0
        self.paused = True
        self.last_update = 0
        self.key_pressed = False
        self.solver = Solver(self)

    def replenish_piecepool(self):
        for pool in self.nextpiecepool:
            if len(pool) < len(SHAPES):
                l = [i for i in range(len(SHAPES)) if i not in pool]
                random.shuffle(l)
                pool += l

    def pop_next_piece(self):
        p = Counter([i for i in range(len(SHAPES))] + self.past_pieces[-len(SHAPES)+2:])
        population = list(p.keys())
        weights = [1 / (x ** 2) for n, x in enumerate(p.values())]
        res = random.choices(population=population, weights=weights)[0]
        self.past_pieces.append(res)
        return res

    def draw_grid(self, screen):
        for y, row in enumerate(self.grid):
            for x, color in enumerate(row):
                pygame.draw.rect(screen, color, (x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE))
                pygame.draw.rect(screen, GRAY, (x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE), 1)

    def draw_piece(self, screen):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    pygame.draw.rect(screen, self.current_piece.color,
                                     ((self.current_piece.x + x) * BLOCK_SIZE, (self.current_piece.y + y) * BLOCK_SIZE,
                                      BLOCK_SIZE, BLOCK_SIZE))

    def draw_paths(self, screen):
        for i in self.solver.all_possible_places:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, RED, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))

        for i in self.solver.past_pos:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, GRAY, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))

        for i in self.solver.possible_placings_pathfound:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, WHITE, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))

        for i in self.solver.piece_path:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, GREEN, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))


        x = self.current_piece.x
        y = self.current_piece.y
        r = self.current_piece.rotation
        pygame.draw.rect(screen, WHITE, ((x * BLOCK_SIZE + BLOCK_SIZE // 5 + (
                    (BLOCK_SIZE // 5) * (r // 2) * 2), y * BLOCK_SIZE + BLOCK_SIZE // 5 + (
                                                     (BLOCK_SIZE // 5) * (r % 2) * 2)),
                                        (BLOCK_SIZE // 5, BLOCK_SIZE // 5)))
        # for y, row in enumerate(self.current_piece.tetromino):
        #     for x, cell in enumerate(row):
        #         if cell:
        #             pygame.draw.rect(screen, (self.current_piece.color[0], self.current_piece.color[0], self.current_piece.color[0], 128),
        #                              ((self.current_piece.x + x) * BLOCK_SIZE, (self.current_piece.y + y) * BLOCK_SIZE,
        #                               BLOCK_SIZE, BLOCK_SIZE))

    def draw_placment_preview(self, screen):
        pos = [self.current_piece.x, self.current_piece.y, self.current_piece.rotation]
        try:
            pos = self.solver.possible_placings_pathfound[self.solver.placment_previeing]
        except IndexError:
            return
        finally:
            piece = Tetrimino(self.current_piece.shape_index, pos[0], pos[1], pos[2])
            for y, row in enumerate(piece.tetromino):
                for x, cell in enumerate(row):
                    if cell:
                        pygame.draw.rect(screen,  Color(self.current_piece.color)//Color(4, 4, 4),
                                         ((pos[0] + x) * BLOCK_SIZE, (pos[1] + y) * BLOCK_SIZE,
                                          BLOCK_SIZE, BLOCK_SIZE))



    def check_collision(self, dx=0, dy=0):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    new_x, new_y = self.current_piece.x + x + dx, self.current_piece.y + y + dy
                    if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (
                            new_y >= 0 and self.grid[new_y][new_x] != BLACK):
                        return True
        return False

    def move_piece(self, dx, dy):
        if not self.check_collision(dx, dy):
            self.current_piece.x += dx
            self.current_piece.y += dy
        elif dy > 0:
            self.place_piece()

    def place_piece(self):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    self.grid[self.current_piece.y + y][self.current_piece.x + x] = self.current_piece.color
        self.clear_lines()
        self.current_piece = Tetrimino(shape_index=self.pop_next_piece())
        if self.check_collision():
            self.running = False  # Game Over
        self.solver.get_possible_placements()

    def rotate(self):
        old_tetromino = self.current_piece.tetromino
        old_rotation = self.current_piece.rotation
        self.current_piece.rotation = (self.current_piece.rotation - 1) % len(self.current_piece.shape)
        self.current_piece.tetromino = self.current_piece.shape[self.current_piece.rotation]
        if self.check_collision():
             self.current_piece.tetromino = old_tetromino
             self.current_piece.rotation = old_rotation


    def clear_lines(self):
        new_grid = [row for row in self.grid if BLACK in row]
        lines_cleared = ROWS - len(new_grid)
        self.score += lines_cleared * 100
        self.grid = [[BLACK] * COLUMNS for _ in range(lines_cleared)] + new_grid

    def update(self):
        self.move_piece(0, 1)


def point_distance(x1, y1, z1, x2, y2, z2):
    return sqrt((x1-x2)**2 + (y1-y2)**2 + (z1-z2)**2)


class Solver:

    def __init__(self, game: Tetris):
        self.thread_active = None
        self.current_piece = Tetrimino()
        self.placment_previeing = 0
        self.game = game
        self.possible_placings_pathfound = []
        self.all_possible_places = []
        self.past_pos = []
        self.piece_path = []
        self.best_pos = []
        self.best_pos_score = 0

    def check_collision(self, dx=0, dy=0):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    new_x, new_y = self.current_piece.x + x + dx, self.current_piece.y + y + dy
                    if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.game.grid[new_y][new_x] != BLACK):
                        return True
        return False

    def calc_pos_score(self, tetromino=None):
        if tetromino is None:
            tetromino = self.current_piece
        # score = 0
        # for y, row in enumerate(tetromino.tetromino):
        #     for x, cell in enumerate(row):
        #         if cell:
        #             new_x, new_y = tetromino.x + x + 1, tetromino.y + y
        #             if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.game.grid[new_y][new_x] != BLACK and (x+1 >= len(row) or not tetromino.tetromino[x+1][y])):
        #                 score += 1
        #             new_x, new_y = tetromino.x + x, tetromino.y + y + 1
        #             if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.game.grid[new_y][new_x] != BLACK and (y+1 >= len(tetromino.tetromino) or not tetromino.tetromino[x+1][y])):
        #                 score += 1
        #             new_x, new_y = tetromino.x + x - 1, tetromino.y + y
        #             if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.game.grid[new_y][new_x] != BLACK and (x-1 <= 0 or not tetromino.tetromino[x+1][y])):
        #                 score += 1
        #             new_x, new_y = tetromino.x + x, tetromino.y + y - 1
        #             if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.game.grid[new_y][new_x] != BLACK and (y+1 >= 0 or not tetromino.tetromino[x+1][y])):
        #                 score += 1
        # if score > self.best_pos_score:
        #     self.best_pos = [tetromino.x, tetromino.y, tetromino.rotation]

    def get_possible_placements(self):

        self.past_pos.clear()
        self.possible_placings_pathfound.clear()
        self.all_possible_places.clear()
        self.placment_previeing = 0
        self.current_piece.copy(self.game.current_piece)

        self.current_piece.x = 0
        self.current_piece.y = 0
        for r in range(len(self.current_piece.shape)):
            self.current_piece.rotate(r)
            for x in range(-2, COLUMNS+2):
                for y in range(ROWS):
                    if not self.check_collision(x, y) and self.check_collision(x, y+1):
                        self.all_possible_places.append([x, y, r])

        self.current_piece.copy(self.game.current_piece)

        self.thread_active = False
        time.sleep(0.1)
        self.thread_active = True
        threading.Thread(target=lambda : self.descend([self.current_piece.x, self.current_piece.y, self.current_piece.rotation])).start()

    def descend(self, pos):
        if not self.thread_active:
            return
        self.current_piece.x = pos[0]
        self.current_piece.y = pos[1]
        self.current_piece.rotate(pos[2])
        pos = [pos[0], pos[1], self.current_piece.rotation]

        if self.check_collision():
            return

        if self.past_pos.count(pos) != 0:
            return

        self.past_pos.append(pos)

        if self.check_collision(0, 1) and self.possible_placings_pathfound.count(pos) <= 0:
            self.possible_placings_pathfound.append(pos)
            # self.calc_pos_score()
            # self.placment_previeing = self.possible_placings_pathfound.index(self.best_pos)

        # time.sleep(0.03)
        arglist = [[pos[0], pos[1], self.current_piece.rotation + 1],
                   [pos[0], pos[1], self.current_piece.rotation - 1],
                   [pos[0], pos[1] + 1, self.current_piece.rotation],
                   [pos[0] + 1, pos[1], self.current_piece.rotation],
                   [pos[0] - 1, pos[1], self.current_piece.rotation]]
        # if len(self.current_piece.shape) > 1:
        #     for i in range(len(self.current_piece.shape)):
        #         arglist.append([pos[0], pos[1], i])
        random.shuffle(arglist)
        for i in arglist:
            self.descend(i)

    def pos_pathfinder(self, game):
        goal = self.possible_placings_pathfound[self.placment_previeing]

        # l = []
        # height_threshhold =
        # for i in self.past_pos:
        #
        # self.past_pos = l

        threading.Thread(target=lambda: self.pos_pathfinder_thread(game, goal)).start()

    def pos_pathfinder_thread(self, game, goal):
        self.current_piece.copy(self.game.current_piece)

        self.thread_active = False
        time.sleep(0.1)
        self.thread_active = True
        # threading.Thread(target=lambda : descend([self.current_piece.x, self.current_piece.y, self.current_piece.rotation])).start()

        queue = []

        destination_not_reached = True

        pos = [self.current_piece.x, self.current_piece.y, self.current_piece.rotation]

        def append(target: list, a: list):
            for i in range(len(target)):
                if a[0] == target[i][0]:
                    if a[3] < target[i][3]:
                        target.pop(i)
                        target.insert(i, a)
                    return
            target.insert(0, a)

        if pos == goal:
            destination_not_reached = False

        queue.append([pos, [], point_distance(*pos, *goal), 0])

        while destination_not_reached:

            print(len(queue))
            l = min(queue, key=lambda x: x[2] + x[3] + (x[2] / 20))
            pos, log, h, f = l
            queue.remove(l)

            self.piece_path = []
            for i in queue:
                self.piece_path.extend(i[1])

            if pos == goal:
                destination_not_reached = False
                self.piece_path = log + [pos]
                break

            t = [pos[0]+1, pos[1], pos[2]]
            if t in self.past_pos and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f+(0 if pos[1] == 0 else 1)])

            t = [pos[0]-1, pos[1], pos[2]]
            if t in self.past_pos and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f+(0 if pos[1] == 0 else 1)])

            t = [pos[0], pos[1]+1, pos[2]]
            if t in self.past_pos and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f+1])

            t = [pos[0], pos[1], self.current_piece.modulo_rotation(pos[2] + 1)]
            if t in self.past_pos and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f+1])

            t = [pos[0], pos[1], self.current_piece.modulo_rotation(pos[2] - 1)]
            if t in self.past_pos and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f+(0 if pos[1] == 0 else 1)])



# Game Loop
def main():
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    clock = pygame.time.Clock()
    game = Tetris()
    font = pygame.font.Font(None, 36)

    while game.running:
        screen.fill(BLACK)
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                game.running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_TAB:
                    game.paused = not game.paused
                    game.key_pressed = True
                if event.key == pygame.K_LEFT:
                    game.move_piece(-1, 0)
                elif event.key == pygame.K_RIGHT:
                    game.move_piece(1, 0)
                elif event.key == pygame.K_DOWN:
                    game.move_piece(0, 1)
                elif event.key == pygame.K_UP:
                    game.move_piece(0, -1)
                elif event.key == pygame.K_x:
                    game.rotate()
                elif event.key == pygame.K_SPACE:
                    piece = game.current_piece
                    while game.current_piece == piece and game:
                        game.move_piece(0, 1)
                elif event.key == pygame.K_p:
                    game.paused=True
                    game.solver.get_possible_placements()
                elif event.key == pygame.K_MINUS:
                    game.solver.placment_previeing -= 1
                elif event.key == pygame.K_EQUALS:
                    game.solver.placment_previeing += 1
                elif event.key == pygame.K_o:
                    game.solver.pos_pathfinder(game)


        if game.last_update + 1 < time.time():

            game.last_update = time.time()

            if not game.paused:
                game.update()
        game.draw_grid(screen)
        game.draw_placment_preview(screen)
        game.draw_piece(screen)
        game.draw_paths(screen)

        # Display score
        if game.paused:
            pygame.draw.rect(screen, WHITE, ((10, 10), (20, 20)))
        else:
            pygame.draw.polygon(screen, WHITE, ((10, 10), (30, 20), (10, 30)))

        to_render = f"Score: {game.score}  {game.solver.placment_previeing}/{len(game.solver.possible_placings_pathfound)}"
        score_text = font.render(to_render, True, WHITE)
        screen.blit(score_text, (50, 10))

        pygame.display.flip()

    pygame.quit()


if __name__ == "__main__":
    main()
